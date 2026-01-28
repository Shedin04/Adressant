package com.shedin.adressant.sender;

import com.shedin.adressant.sender.headers.EmailHeadersRequest;
import com.shedin.adressant.sender.headers.EmailHeadersResponse;
import com.shedin.adressant.sender.headers.HeadersAnalyzerService;
import com.shedin.adressant.sender.userid.UuidService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.shedin.adressant.sender.helpers.RegexHelper.EMAIL_REGEX;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "api/v1/sender")
@CrossOrigin
@Log4j2
public class SenderController {

    private final SenderService senderService;
    private final UuidService uuidService;
    private final HeadersAnalyzerService headersAnalyzerService;

    @GetMapping
    public SenderResponse getEmailInfo(@RequestHeader(value = "X-Request-ID") UUID userId,
                                       @NotNull @NotEmpty @Email(message = "Email is not valid", regexp = EMAIL_REGEX) @RequestHeader String email) {
        log.info("New get email request from '{}' uuid", userId.toString());
        return senderService.getSenderInfo(email);
    }

    @PutMapping(path = "rating", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailRating> updateEmailRating(@Valid @RequestBody EmailRating emailRating,
                                                         @RequestHeader(value = "X-Request-ID") UUID userId) {
        log.info("New update rating request from '{}' uuid", userId.toString());
        if (uuidService.isBlockedUserId(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            EmailRating response = senderService.updateEmailRating(emailRating);
            uuidService.blockUserById(userId);
            return Optional.ofNullable(response).map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
    }

    @PostMapping(path = "process-headers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmailHeadersResponse processEmailHeaders(@Valid @RequestBody EmailHeadersRequest emailHeadersRequest,
                                                    @RequestHeader(value = "X-Request-ID") UUID userId) {
        log.info("New process headers request from '{}' uuid", userId.toString());
        return headersAnalyzerService.processHeaders(emailHeadersRequest);
    }
}