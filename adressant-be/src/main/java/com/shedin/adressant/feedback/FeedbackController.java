package com.shedin.adressant.feedback;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping(path = "api/v1/ai-feedback")
@CrossOrigin
@Log4j2
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedbackResponse> storeFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            @RequestHeader(value = "X-Request-ID") UUID userId) {
        log.info("New feedback request from '{}' uuid", userId.toString());
        FeedbackResponse feedbackResponse = feedbackService.writeFeedbackToCsv(feedbackRequest.getAnalyzedText(), feedbackRequest.getActualPrediction(), feedbackRequest.getIsValid());
        return ResponseEntity.ok(feedbackResponse);
    }
}