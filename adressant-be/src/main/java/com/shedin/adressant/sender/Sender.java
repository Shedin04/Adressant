package com.shedin.adressant.sender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sender {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String domainId;
    @Column(length = 320, nullable = false, unique = true)
    @NonNull
    private String domainName;
    @Column(columnDefinition = "Decimal(3,1) default '0.0'", nullable = false)
    private double domainRating;
    @Column(columnDefinition = "int default 0", nullable = false)
    private int numberOfRating;
    @Column(nullable = false)
    @NonNull
    private boolean isTemporary;
    @Column(nullable = false)
    @NonNull
    private boolean isFree;
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private LocalDateTime lastScanTime;
    @Column(columnDefinition = "int default 0", nullable = false)
    private int numberOfScans;
}
