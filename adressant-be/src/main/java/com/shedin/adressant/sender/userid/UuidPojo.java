package com.shedin.adressant.sender.userid;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UuidPojo {

    @Id
    @Column(nullable = false, columnDefinition = "VARCHAR(36)")
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
            message = "Invalid UUID provided")
    @NonNull
    private String userId;
    @Column(nullable = false)
    @NonNull
    private boolean isBlocked;
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private LocalDateTime lastBlockTime;
}
