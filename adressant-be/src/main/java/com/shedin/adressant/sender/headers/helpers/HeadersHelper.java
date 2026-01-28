package com.shedin.adressant.sender.headers.helpers;

import com.shedin.adressant.sender.headers.AddressDetails;
import com.shedin.adressant.sender.headers.AuthenticationDetails;
import com.shedin.adressant.sender.headers.ContentDetails;
import com.shedin.adressant.sender.headers.EmailHeaders;
import com.shedin.adressant.sender.headers.MessageDetails;
import com.shedin.adressant.sender.headers.TransferDetails;
import com.shedin.adressant.sender.headers.TransferPoint;
import com.shedin.adressant.sender.helpers.RegexHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import static java.time.ZoneOffset.UTC;

@Log4j2
@Component
@RequiredArgsConstructor
public class HeadersHelper {
    public static final String COMMA = ",";
    private final RegexHelper regexHelper;
    private InternetHeaders headers;
    private final CopyOnWriteArraySet<String> processedHeaders = new CopyOnWriteArraySet<>();
    private static final Pattern FROM_REGEX = Pattern.compile("^(.*)\\s+(\\S+)$");
    private final TransferPointHelper transferPointHelper;
    private final SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public void setEmailHeaders(String emailHeaders) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(emailHeaders.getBytes(StandardCharsets.UTF_8))) {
            headers = new InternetHeaders(input);
        } catch (IOException | MessagingException e) {
            log.warn("Failed to load headers: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String processHeader(String headerName) {
        String headerValue = headers.getHeader(headerName, COMMA);
        if (headerValue == null) {
            return null;
        }
        processedHeaders.add(headerName);
        return processValue(headerValue);
    }

    private Pair<String, String> getPairForHeader(String headerName, Pattern pattern) {
        String fromHeader = processHeader(headerName);
        boolean isMoreThanOneHeader = Optional.ofNullable(fromHeader).map(header -> header.split(COMMA).length > 1).orElse(false);
        String firstValue = isMoreThanOneHeader ? fromHeader : regexHelper.getFirstGroup(pattern, fromHeader);
        String secondValue = isMoreThanOneHeader ? fromHeader : regexHelper.getSecondGroup(pattern, fromHeader);
        if (firstValue == null) {
            throw new RuntimeException("Invalid 'From' header");
        }
        return Pair.of(firstValue, secondValue);
    }

    private String processValue(String headerValue) {
        headerValue = Optional.ofNullable(headerValue).orElse(StringUtils.EMPTY);
        byte[] bytes = headerValue.getBytes(StandardCharsets.ISO_8859_1);
        return StringUtils.normalizeSpace(new String(bytes, StandardCharsets.UTF_8).replaceAll("(\")|(<)|(>)|(\r)|(\n)", StringUtils.EMPTY));
    }

    public AddressDetails getAddressDetails() {
        Pair<String, String> fromHeaderPair = getPairForHeader(EmailHeaders.FROM, FROM_REGEX);
        String mailFromName = fromHeaderPair.getFirst();
        String mailFrom = fromHeaderPair.getSecond();
        String resentFrom = processHeader(EmailHeaders.RESENT_FROM);
        String sender = processHeader(EmailHeaders.SENDER);
        String resentSender = processHeader(EmailHeaders.RESENT_SENDER);
        String replyTo = processHeader(EmailHeaders.REPLY_TO);
        String to = processHeader(EmailHeaders.TO);
        String deliveredTo = processHeader(EmailHeaders.DELIVERED_TO);
        String resentTo = processHeader(EmailHeaders.RESENT_TO);
        String returnPath = processHeader(EmailHeaders.RETURN_PATH);
        String dispositionNotificationTo = processHeader(EmailHeaders.DISPOSITION_NOTIFICATION_TO);
        String carbonCopy = processHeader(EmailHeaders.CC);
        String blindCarbonCopy = processHeader(EmailHeaders.BCC);
        String resentCarbonCopy = processHeader(EmailHeaders.RESENT_CC);
        String resentBlindCarbonCopy = processHeader(EmailHeaders.RESENT_BCC);
        return AddressDetails.builder()
                .mailFrom(mailFrom)
                .mailFromName(mailFromName)
                .resentFrom(resentFrom)
                .sender(sender)
                .resentSender(resentSender)
                .mailTo(to)
                .deliveredTo(deliveredTo)
                .replyTo(replyTo)
                .resentTo(resentTo)
                .returnPath(returnPath)
                .dispositionNotificationTo(dispositionNotificationTo)
                .carbonCopy(carbonCopy)
                .blindCarbonCopy(blindCarbonCopy)
                .resentCarbonCopy(resentCarbonCopy)
                .resentBlindCarbonCopy(resentBlindCarbonCopy)
                .build();
    }

    public Map<String, String> getUnsortedHeaders() {
        Map<String, String> headerMap = new TreeMap<>();
        Enumeration<?> allHeaders = headers.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            if (processedHeaders.stream().noneMatch(header.getName()::equalsIgnoreCase)) {
                headerMap.put(header.getName(), processValue(header.getValue()));
            }
        }
        return headerMap.isEmpty() ? null : headerMap;
    }

    public MessageDetails getMessageDetails() {
        String subject = processHeader(EmailHeaders.SUBJECT);
        String messageId = processHeader(EmailHeaders.MESSAGE_ID);
        String date = processHeader(EmailHeaders.DATE);
        formatter.setTimeZone(TimeZone.getTimeZone(UTC));
        String dateInUtc;
        try {
            Date utc = formatter.parse(date);
            dateInUtc = formatter.format(utc);
            if (Objects.equals(date, dateInUtc)) {
                dateInUtc = null;
            }
        } catch (ParseException e) {
            dateInUtc = null;
        }
        String messageContext = processHeader(EmailHeaders.MESSAGE_CONTEXT);
        String messageType = processHeader(EmailHeaders.MESSAGE_TYPE);
        String originalMessageId = processHeader(EmailHeaders.ORIGINAL_MESSAGE_ID);
        String resentMessageId = processHeader(EmailHeaders.RESENT_MESSAGE_ID);
        String inReplyTo = processHeader(EmailHeaders.IN_REPLY_TO);
        String references = processHeader(EmailHeaders.REFERENCES);
        return MessageDetails.builder()
                .subject(subject)
                .messageId(messageId)
                .date(date)
                .dateInUtc(dateInUtc)
                .messageContext(messageContext)
                .messageType(messageType)
                .originalMessageId(originalMessageId)
                .resentMessageId(resentMessageId)
                .inReplyTo(inReplyTo)
                .references(references)
                .build();
    }

    public ContentDetails getContentDetails() {
        String contentType = processHeader(EmailHeaders.CONTENT_TYPE);
        String contentTranslationType = processHeader(EmailHeaders.CONTENT_TRANSLATION_TYPE);
        String contentDisposition = processHeader(EmailHeaders.CONTENT_DISPOSITION);
        String contentTransferEncoding = processHeader(EmailHeaders.CONTENT_TRANSFER_ENCODING);
        String contentID = processHeader(EmailHeaders.CONTENT_ID);
        String contentIdentifier = processHeader(EmailHeaders.CONTENT_IDENTIFIER);
        String contentDescription = processHeader(EmailHeaders.CONTENT_DESCRIPTION);
        String contentLanguage = processHeader(EmailHeaders.CONTENT_LANGUAGE);
        String contentBase = processHeader(EmailHeaders.CONTENT_BASE);
        String contentReturn = processHeader(EmailHeaders.CONTENT_RETURN);
        String contentMD5 = processHeader(EmailHeaders.CONTENT_MD5);
        String contentLocation = processHeader(EmailHeaders.CONTENT_LOCATION);
        String contentFeatures = processHeader(EmailHeaders.CONTENT_FEATURES);
        String contentDuration = processHeader(EmailHeaders.CONTENT_DURATION);
        String contentAlternative = processHeader(EmailHeaders.CONTENT_ALTERNATIVE);
        String mimeVersion = processHeader(EmailHeaders.MIME_VERSION);
        return ContentDetails.builder()
                .contentType(contentType)
                .contentTranslationType(contentTranslationType)
                .contentDisposition(contentDisposition)
                .contentTransferEncoding(contentTransferEncoding)
                .contentID(contentID)
                .contentIdentifier(contentIdentifier)
                .contentDescription(contentDescription)
                .contentLanguage(contentLanguage)
                .contentBase(contentBase)
                .contentReturn(contentReturn)
                .contentMD5(contentMD5)
                .contentLocation(contentLocation)
                .contentFeatures(contentFeatures)
                .contentDuration(contentDuration)
                .contentAlternative(contentAlternative)
                .mimeVersion(mimeVersion)
                .build();
    }

    public TransferDetails getTransferDetails() {
        String[] receivedHeaders = headers.getHeader(EmailHeaders.RECEIVED);
        String[] xReceivedHeaders = headers.getHeader(EmailHeaders.X_RECEIVED);
        processedHeaders.addAll(List.of(EmailHeaders.RECEIVED, EmailHeaders.X_RECEIVED));
        List<TransferPoint> transferPoints = null;
        String totalMessageDelay = null;
        if (receivedHeaders != null) {
            String[] allReceivedHeaders = receivedHeaders;
            if (xReceivedHeaders != null) {
                allReceivedHeaders = Arrays.copyOf(receivedHeaders, receivedHeaders.length + xReceivedHeaders.length);
                System.arraycopy(xReceivedHeaders, 0, allReceivedHeaders, receivedHeaders.length, xReceivedHeaders.length);
            }
            List<String> processedReceivedHeaders = new ArrayList<>();
            for (String header : allReceivedHeaders) {
                String processedHeader = processValue(header);
                processedReceivedHeaders.add(processedHeader);
            }
            transferPoints = transferPointHelper.getTransferPoints(processedReceivedHeaders);
            totalMessageDelay = transferPointHelper.getTotalDelay(transferPoints);
        }
        return TransferDetails.builder()
                .transferPoints(transferPoints)
                .totalMessageDelay(totalMessageDelay)
                .build();
    }

    public AuthenticationDetails getAuthenticationDetails() {
        String authenticationResults = processHeader(EmailHeaders.AUTHENTICATION_RESULTS);
        String arcAuthenticationResults = processHeader(EmailHeaders.ARC_AUTHENTICATION_RESULTS);
        String arcSeal = processHeader(EmailHeaders.ARC_SEAL);
        String arcMessageSignature = processHeader(EmailHeaders.ARC_MESSAGE_SIGNATURE);
        String receivedSpf = processHeader(EmailHeaders.RECEIVED_SPF);
        String dkimSignature = processHeader(EmailHeaders.DKIM_SIGNATURE);
        return AuthenticationDetails.builder()
                .authenticationResults(authenticationResults)
                .arcAuthenticationResults(arcAuthenticationResults)
                .arcSeal(arcSeal)
                .arcMessageSignature(arcMessageSignature)
                .receivedSpf(receivedSpf)
                .dkimSignature(dkimSignature)
                .build();
    }

    public Map<String, String> getXHeaders() {
        Map<String, String> xHeaders = new TreeMap<>();
        Enumeration<?> allHeaders = headers.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            String headerName = header.getName();
            if (headerName.startsWith("X-")) {
                xHeaders.put(headerName, processHeader(headerName));
            }
        }
        return xHeaders.isEmpty() ? null : xHeaders;
    }
}
