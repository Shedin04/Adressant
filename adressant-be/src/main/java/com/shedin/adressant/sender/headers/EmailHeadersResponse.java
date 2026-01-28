package com.shedin.adressant.sender.headers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class EmailHeadersResponse {

    private AddressDetails addressDetails;
    private TransferDetails transferDetails;
    private MessageDetails messageDetails;
    private ContentDetails contentDetails;
    private AuthenticationDetails authenticationDetails;
    private Map<String, String> xHeaders;
    private List<String> links;
    private Map<String, List<String>> ipWhois;
    private Map<String, String> unsortedHeaders;
}
