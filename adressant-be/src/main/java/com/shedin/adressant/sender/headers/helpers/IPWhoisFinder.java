package com.shedin.adressant.sender.headers.helpers;

import com.shedin.adressant.sender.helpers.RegexHelper;
import com.shedin.adressant.sender.helpers.WhoisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Lazy
@RequiredArgsConstructor
public class IPWhoisFinder {

    private final RegexHelper regexHelper;
    private final WhoisHelper whoisHelper;

    public Map<String, List<String>> getAllIPsWhois(String headersData) {
        Set<String> ipSet = regexHelper.extractIPs(headersData);
        return ipSet.isEmpty() ? null : ipSet.stream().collect(Collectors.toMap(ip -> ip, whoisHelper::getIPWhoisLookup));
    }
}
