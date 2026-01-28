package com.shedin.adressant.sender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SenderRepository extends JpaRepository<Sender, String> {

    Optional<Sender> findByDomainName(String domainName);
    Optional<Sender> findByDomainId(String domainId);
}