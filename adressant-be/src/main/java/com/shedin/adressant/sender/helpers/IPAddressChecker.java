package com.shedin.adressant.sender.helpers;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@NoArgsConstructor
@Log4j2
public class IPAddressChecker {

    public String getDomainIP(String domain) {
        try {
            InetAddress address = InetAddress.getByName(domain);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("No IP address found for '{}': '{}'", domain, e.getMessage());
            return "No IP found";
        }
    }
}