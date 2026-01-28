package com.shedin.adressant.sender.headers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailHeadersRequest {

    @NotNull
    @NotBlank
    @Pattern(regexp = "(gmail|outlook)", message = "Invalid service provided")
    public String service;

    @NotNull
    @NotBlank(message = "Headers are required")
    @Pattern(regexp = "(?s)^(Delivered-To:|Received: from|From:|MIME-Version:|Date:).*$", message = "Invalid headers data provided")
    public String headersData;
}
