package com.shedin.adressant.sender.headers.strategy;

import com.shedin.adressant.sender.headers.EmailHeadersResponse;

public interface ServiceStrategy {

    EmailHeadersResponse processHeaders(String headers);

    StrategyName getServiceName();
}