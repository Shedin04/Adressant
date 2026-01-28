package com.shedin.adressant.sender.userid;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class UuidService {

    @Value("${time.to.block.userid}")
    private long timeToBlock;
    private final UuidRepository uuidRepository;

    @Autowired
    public UuidService(UuidRepository uuidRepository) {
        this.uuidRepository = uuidRepository;
    }

    public boolean isBlockedUserId(UUID userId) {
        String uuid = userId.toString();
        Optional<UuidPojo> uuidOptional = uuidRepository.findById(uuid);
        if (uuidOptional.isEmpty()) {
            UuidPojo newUuidPojo = new UuidPojo(uuid, false, LocalDateTime.now(ZoneOffset.UTC));
            uuidRepository.save(newUuidPojo);
            return false;
        } else {
            UuidPojo existingUuidPojo = uuidOptional.get();
            if (!existingUuidPojo.isBlocked()) {
                return false;
            } else {
                LocalDateTime lastBlockTime = existingUuidPojo.getLastBlockTime();
                boolean isExpectedTimeNotPassed = !isExpectedTimePassed(lastBlockTime);
                if (isExpectedTimeNotPassed) {
                    log.warn("The user with '{}' is still blocked", uuid);
                } else {
                    existingUuidPojo.setBlocked(false);
                    uuidRepository.save(existingUuidPojo);
                }
                return isExpectedTimeNotPassed;
            }
        }
    }

    private boolean isExpectedTimePassed(LocalDateTime lastBlockTime) {
        return lastBlockTime.until(LocalDateTime.now(), ChronoUnit.MINUTES) >= timeToBlock;
    }

    public void blockUserById(UUID userId) {
        String uuid = userId.toString();
        Optional<UuidPojo> uuidOptional = uuidRepository.findById(uuid);
        if (uuidOptional.isPresent()) {
            UuidPojo uuidPojoToUpdate = uuidOptional.get();
            uuidPojoToUpdate.setBlocked(true);
            uuidPojoToUpdate.setLastBlockTime(LocalDateTime.now());
            uuidRepository.save(uuidPojoToUpdate);
            log.info("'{}' userid was blocked on {} minutes", uuid, timeToBlock);
        } else {
            log.warn("Cannot find '{}' userId in DB", uuid);
        }
    }
}