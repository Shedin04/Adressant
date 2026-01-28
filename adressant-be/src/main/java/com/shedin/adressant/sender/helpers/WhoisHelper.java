package com.shedin.adressant.sender.helpers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.whois.WhoisClient;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Component
@AllArgsConstructor
@Log4j2
public class WhoisHelper {

    private RegexHelper regexHelper;
    private static final List<String> NO_WHOIS_ERROR = List.of("No WHOIS information was found!");
    private static final List<String> WHOIS_SERVER_ERROR = List.of("No WHOIS information was found. Please, try again");
    private static final String NOT_MANAGED_BY_THE_RIPE = "not managed by the RIPE";
    private static final String IANA_WHOIS_URL = "whois.iana.org";
    private static final String RIPE_WHOIS_URL = "whois.ripe.net";
    private static final String ARIN_WHOIS_URL = "whois.arin.net";

    public List<String> getDomainWhoisLookup(String domain) {
        return getWhoisLookup(domain, IANA_WHOIS_URL);
    }

    public List<String> getIPWhoisLookup(String ip) {
        List<String> whoisResult = getWhoisLookup(ip, RIPE_WHOIS_URL);
        return shouldTryAnotherWhois(whoisResult) ? getWhoisLookup(ip, ARIN_WHOIS_URL) : whoisResult;
    }

    private boolean shouldTryAnotherWhois(List<String> whoisResult) {
        return whoisResult.stream().anyMatch(r -> r.contains(NOT_MANAGED_BY_THE_RIPE) || r.contains(NO_WHOIS_ERROR.get(0)));
    }

    private List<String> getWhoisLookup(String handle, String whoisURL) {
        List<String> whoisResponse;
        WhoisClient whoisClient = new WhoisClient();
        try {
            whoisClient.connect(whoisURL);
            String whoisData = whoisClient.query(handle);
            whoisClient.disconnect();
            whoisResponse = regexHelper.getWhoisFromResponse(whoisData);
        } catch (Exception e) {
            log.warn("Failed to get WHOIS information for '{}' due to '{}'. Retrying...", handle, e.getMessage());
            try {
                whoisClient.connect(whoisURL);
                String whoisData = whoisClient.query(handle);
                whoisClient.disconnect();
                whoisResponse = regexHelper.getWhoisFromResponse(whoisData);
            } catch (Exception ex) {
                log.warn("Failed to get WHOIS information for '{}' again due to '{}'", handle, ex.getMessage());
                return WHOIS_SERVER_ERROR;
            }
        }
        return whoisResponse == null ? NO_WHOIS_ERROR : processWhoisResponse(whoisResponse);
    }

    private List<String> processWhoisResponse(List<String> whoisToProcess) {
        List<String> processResult = whoisToProcess.stream()
                .filter(row -> !row.isEmpty())
                .map(String::trim)
                .map(row -> row.replaceAll("\\s+", SPACE))
                .filter(row -> row.length() < 85)
                .toList();
        return processResult.isEmpty() ? NO_WHOIS_ERROR : processResult;
    }
}