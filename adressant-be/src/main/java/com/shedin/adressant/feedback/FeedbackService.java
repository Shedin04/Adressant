package com.shedin.adressant.feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

@Service
@Log4j2
@RequiredArgsConstructor
public class FeedbackService {

    @Value("${feedback.file}")
    private String feedbackFile;
    private static final String[] CSV_HEADER = {"timestamp", "analyzed_text", "actual_prediction", "is_valid", "is_feedback_valid"};
    private static final String TO_UPDATE_PLACEHOLDER = "[TO_UPDATE]";

    public FeedbackResponse writeFeedbackToCsv(String analyzedText, String actualPrediction, Boolean isValid) {
        String timestamp = Instant.now().toString();
        Object[] csvRecord = {
                timestamp,
                analyzedText,
                actualPrediction,
                isValid,
                TO_UPDATE_PLACEHOLDER
        };

        File csvFile = new File(feedbackFile);
        boolean isNewFile = !csvFile.exists() || csvFile.length() == 0;

        CSVFormat csvFormat = isNewFile
                ? CSVFormat.Builder.create(CSVFormat.RFC4180).setHeader(CSV_HEADER).get()
                : CSVFormat.Builder.create(CSVFormat.RFC4180).setSkipHeaderRecord(true).get();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(feedbackFile, true));
             CSVPrinter csvPrinter = new CSVPrinter(bw, csvFormat)) {
            csvPrinter.printRecord(csvRecord);
            csvPrinter.flush();
            log.info("Feedback stored for text: {}...", analyzedText.substring(0, Math.min(50, analyzedText.length())));
            return FeedbackResponse.builder().status("success").build();
        } catch (IOException e) {
            log.error("Error writing feedback to CSV: {}", e.getMessage());
            return FeedbackResponse.builder().status("fail").build();
        }
    }
}