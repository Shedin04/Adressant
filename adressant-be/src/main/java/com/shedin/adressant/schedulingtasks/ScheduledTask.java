package com.shedin.adressant.schedulingtasks;

import com.shedin.adressant.sender.userid.UuidRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduledTask {

    @NonNull
    private UuidRepository uuidRepository;
    @Value("${time.to.block.userid}")
    private long timeToBlock;

    @Scheduled(fixedDelay = 1800000)
    public void updateBlockedStatusForUuid() {
        log.info("Started updating uuid block statuses");
        LocalDateTime expectedTime = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(timeToBlock);
        uuidRepository.updateBlockedStatusIfExpectedTimePassed(expectedTime);
    }
}