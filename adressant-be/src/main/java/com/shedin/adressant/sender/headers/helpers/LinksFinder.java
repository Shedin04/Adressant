package com.shedin.adressant.sender.headers.helpers;

import com.shedin.adressant.sender.helpers.RegexHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
@RequiredArgsConstructor
public class LinksFinder {

    private final RegexHelper regexHelper;

    public List<String> getAllUniqueLinks(String headersData) {
        List<String> links = regexHelper.extractLinks(headersData.replaceAll("=[\\r\\n]+", StringUtils.EMPTY)).parallelStream().filter(link -> !link.contains("w3.org")).toList();
        return links.isEmpty() ? null : links;
    }
}
