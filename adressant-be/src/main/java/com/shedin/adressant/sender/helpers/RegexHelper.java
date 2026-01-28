package com.shedin.adressant.sender.helpers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Lazy
public class RegexHelper {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,8}$"; //OWASP regex
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,8})");
    private static final Pattern WHOIS_RESPONSE_PATTERN = Pattern.compile("^(?!.*ERROR)(?:Amazon.*|\\S+:)(?!\\s*$).*$", Pattern.MULTILINE);
    private static final Pattern LINKS_PATTERN = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])");
    private static final Pattern IP_PATTERN = Pattern.compile("(?<![\\d.])(?!255[.](25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-4]|1\\d{2}|[1-9]?\\d))(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-5]|2[0-4]\\d|[01]?\\d{1,2})[.](25[0-4]|1\\d{2}|[1-9]?\\d)(?![\\d.])");
    private static final Object lock = new Object();

    public String getDomainFromEmail(String email) {
        return getFirstGroup(DOMAIN_PATTERN, email);
    }

    public String getFirstGroup(Pattern pattern, String text) {
        synchronized (lock) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            } else return text;
        }
    }

    public String getSecondGroup(Pattern pattern, String text) {
        synchronized (lock) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(2);
            } else return text;
        }
    }

    private List<String> getAllMatches(Pattern pattern, String text) {
        synchronized (lock) { // Synchronize access to shared resource
            List<String> matchList = new ArrayList<>();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                matchList.add(matcher.group());
            }
            return !matchList.isEmpty() ? matchList : null;
        }
    }

    public List<String> getWhoisFromResponse(String response) {
        try {
            return getAllMatches(WHOIS_RESPONSE_PATTERN, response);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Set<String> extractLinks(String text) {
        Matcher matcher = LINKS_PATTERN.matcher(text);
        return matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> extractIPs(String text) {
        Matcher matcher = IP_PATTERN.matcher(text);
        return matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
