package com.shedin.adressant.sender.headers.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StrategyName {
    GMAIL_HEADERS_HANDLER("gmail"),
    OUTLOOK_HEADERS_HANDLER("outlook");

    @Getter
    private final String value;
}