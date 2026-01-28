package com.shedin.adressant.sender.headers.strategy;

import com.shedin.adressant.sender.headers.EmailHeadersResponse;
import com.shedin.adressant.sender.headers.helpers.HeadersHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@RequiredArgsConstructor
public class GmailHeadersHandler implements ServiceStrategy {

    private final HeadersHelper headersHelper;

    @Override
    public EmailHeadersResponse processHeaders(String headers) {
        headersHelper.setEmailHeaders(headers);
        return EmailHeadersResponse.builder()
                .addressDetails(headersHelper.getAddressDetails())
                .transferDetails(headersHelper.getTransferDetails())
                .messageDetails(headersHelper.getMessageDetails())
                .contentDetails(headersHelper.getContentDetails())
                .authenticationDetails(headersHelper.getAuthenticationDetails())
                .xHeaders(headersHelper.getXHeaders())
                .unsortedHeaders(headersHelper.getUnsortedHeaders())
                .build();
    }

    @Override
    public StrategyName getServiceName() {
        return StrategyName.GMAIL_HEADERS_HANDLER;
    }
}