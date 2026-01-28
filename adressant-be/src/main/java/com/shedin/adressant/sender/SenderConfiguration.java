package com.shedin.adressant.sender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shedin.adressant.sender.helpers.ZonedDateTimeAdapter;
import com.shedin.adressant.sender.helpers.LocalDateTimeAdapter;
import com.shedin.adressant.sender.userid.UuidPojo;
import com.shedin.adressant.sender.userid.UuidRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Configuration
public class SenderConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(SenderRepository senderRepository, UuidRepository uuidRepository) {
        return args -> {
            String testEmail1 = "mailinator.com";
            if (senderRepository.findByDomainName(testEmail1).isEmpty()) {
                LocalDateTime initialScanTime = LocalDateTime.now(ZoneOffset.UTC);
                Sender sender1 = new Sender(testEmail1, false, false, initialScanTime);
                Sender sender2 = new Sender("gmail.com", false, false, initialScanTime);
                senderRepository.saveAll(List.of(sender1, sender2));
            }

            UuidPojo uuidPojo1 = new UuidPojo("11111111-3d0d-6b9b-64d6-dc40e0125bd1", false, LocalDateTime.now(ZoneOffset.UTC));
            UuidPojo uuidPojo2 = new UuidPojo("11111112-3d0d-6b9b-64d6-dc40e0125bd2", true, LocalDateTime.now(ZoneOffset.UTC));
            uuidRepository.saveAll(List.of(uuidPojo1, uuidPojo2));
        };
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
    }
}