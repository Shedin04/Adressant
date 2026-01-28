package com.shedin.adressant.sender.headers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
public class TransferPoint {

    private String details;
    private ZonedDateTime dateTime;
    private ZonedDateTime dateTimeToUTC;
    private String delay;
}
