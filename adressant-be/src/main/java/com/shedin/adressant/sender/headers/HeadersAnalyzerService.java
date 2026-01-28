package com.shedin.adressant.sender.headers;

import com.shedin.adressant.sender.headers.helpers.IPWhoisFinder;
import com.shedin.adressant.sender.headers.helpers.LinksFinder;
import com.shedin.adressant.sender.headers.strategy.ServiceHeadersHandlerFactory;
import com.shedin.adressant.sender.headers.strategy.ServiceStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
@RequiredArgsConstructor
public class HeadersAnalyzerService {

    private final ServiceHeadersHandlerFactory serviceHeadersHandlerFactory;
    private final LinksFinder linksFinder;
    private final IPWhoisFinder ipWhoisFinder;

    public EmailHeadersResponse processHeaders(EmailHeadersRequest emailHeadersRequest) {
        String headersData = emailHeadersRequest.getHeadersData();
        ServiceStrategy serviceHandler = serviceHeadersHandlerFactory.getStrategy(emailHeadersRequest.getService());
        EmailHeadersResponse emailHeadersResponse = serviceHandler.processHeaders(headersData);
        emailHeadersResponse.setLinks(linksFinder.getAllUniqueLinks(headersData));
        emailHeadersResponse.setIpWhois(ipWhoisFinder.getAllIPsWhois(headersData));
        return emailHeadersResponse;
    }
}
