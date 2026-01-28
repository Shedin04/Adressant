package com.shedin.adressant.sender;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SenderResponse {
    @NonNull
    private String domainId;
    @NonNull
    private String domain;
    @NonNull
    private double rate;
    @NonNull
    private int numberOfScans;
    @NonNull
    private LocalDateTime lastScanTime;
    @NonNull
    private boolean isTemporary;
    @NonNull
    private boolean isFree;
    @NonNull
    private String ipAddress;
    @NonNull
    private List<String> mxRecords;
    @NonNull
    private List<String> whoisInfo;
}