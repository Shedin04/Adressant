package com.shedin.adressant.sender;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
public class EmailRating {
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
            message = "Invalid UUID provided")
    @NotBlank(message = "Domain is required")
    private String domainId;
    @NotNull(message = "Rating shouldn't be null")
    @DecimalMax("10.0")
    @DecimalMin("0.1")
    @Digits(integer = 2, fraction = 1, message = "Incorrect rate number format")
    @Setter
    private double domainRating;
}