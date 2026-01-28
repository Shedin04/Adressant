package com.shedin.adressant.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FeedbackRequest {
    @NotBlank(message = "analyzedText is required")
    private String analyzedText;
    @NotBlank(message = "actualPrediction is required")
    private String actualPrediction;
    @NotNull(message = "isValid is required")
    private Boolean isValid;
}