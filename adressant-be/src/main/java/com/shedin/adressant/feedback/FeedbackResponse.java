package com.shedin.adressant.feedback;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeedbackResponse {
    private final String status;
}
