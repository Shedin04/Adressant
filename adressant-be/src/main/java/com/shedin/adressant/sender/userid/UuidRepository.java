package com.shedin.adressant.sender.userid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface UuidRepository extends JpaRepository<UuidPojo, String> {

    @Modifying
    @Transactional
    @Query("UPDATE UuidPojo u SET u.isBlocked = false WHERE u.lastBlockTime <= :expectedTime")
    void updateBlockedStatusIfExpectedTimePassed(LocalDateTime expectedTime);
}