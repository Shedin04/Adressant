package com.shedin.adressant.sender.headers.helpers;

import com.shedin.adressant.sender.headers.TransferPoint;
import com.shedin.adressant.sender.helpers.RegexHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TransferPointHelper {

    private static final String NO_DELAY = "No delay";
    private final RegexHelper regexHelper;
    private static final Pattern RECIEVED_VALUE_PATTERN = Pattern.compile("^(?:from|by|with|id|via|for)(.*);(.*?)(?:\\s*\\((.*)\\))?$");

    public List<TransferPoint> getTransferPoints(List<String> allReceivedHeaders) {
        List<String> skippedHeaders = new ArrayList<>();
        List<TransferPoint> transferPoints = allReceivedHeaders.stream()
                .map(value -> {
                    String details = regexHelper.getFirstGroup(RECIEVED_VALUE_PATTERN, value).trim();
                    String time = regexHelper.getSecondGroup(RECIEVED_VALUE_PATTERN, value).trim();
                    if (details.equals(time)) {
                        skippedHeaders.add(details + " [SKIPPED]");
                        return null;
                    }
                    ZonedDateTime dateTime = null;
                    try {
                        dateTime = ZonedDateTime.parse(time, DateTimeFormatter.RFC_1123_DATE_TIME);
                    } catch (DateTimeParseException e) {
                        try {
                            dateTime = ZonedDateTime.parse(time, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z"));
                        } catch (DateTimeParseException ignored) {
                        }
                    }
                    ZonedDateTime dateTimeToUTC = null;
                    if (dateTime != null) {
                        dateTimeToUTC = dateTime.withZoneSameInstant(ZoneOffset.UTC);
                    }
                    return TransferPoint.builder()
                            .details(details)
                            .dateTime(dateTime)
                            .dateTimeToUTC(dateTimeToUTC)
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(TransferPoint::getDateTime))
                .toList();

        String delayString = NO_DELAY;
        ZonedDateTime prevDateTime = null;
        for (TransferPoint transferPoint : transferPoints) {
            if (prevDateTime != null) {
                Duration duration = Duration.between(prevDateTime, transferPoint.getDateTime());
                delayString = formatDuration(duration);
            }
            prevDateTime = transferPoint.getDateTime();
            transferPoint.setDelay(delayString);
        }
        return Stream.concat(transferPoints.stream(), skippedHeaders.stream().map(h -> TransferPoint.builder().details(h).build())).toList();
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds == 0) {
            return NO_DELAY;
        }
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%dh:%02dm:%02ds",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public String getTotalDelay(List<TransferPoint> transferPoints) {
        if (transferPoints == null || transferPoints.isEmpty()) {
            return NO_DELAY;
        }
        ZonedDateTime startDateTime = transferPoints.get(0).getDateTime();
        ZonedDateTime endDateTime = transferPoints.stream()
                .map(TransferPoint::getDateTime)
                .filter(Objects::nonNull)
                .reduce((a, b) -> b)
                .orElse(startDateTime);
        Duration duration = Duration.between(startDateTime, endDateTime);
        return formatDuration(duration);
    }
}