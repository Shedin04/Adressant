package com.shedin.adressant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdressantApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdressantApplication.class, args);
    }
}
