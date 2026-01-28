package com.shedin.adressant.sender.helpers;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Service
@NoArgsConstructor
@Log4j2
public class MXRecordsChecker {
    private static final String MX = "MX";
    private static final List<String> ERROR_LIST = List.of("No MX records were found!");
    @Value("${dns.server}")
    private String dns;

    public List<String> getMxRecords(String domain) {
        // Get the domain name from the email address
        try {
            // Set the DNS server properties for the JNDI context
            System.setProperty("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            System.setProperty("java.naming.provider.url", "dns:");
            System.setProperty("sun.net.spi.nameservice.nameservers", dns);
            System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");

            // Create the JNDI context
            DirContext ctx = new InitialDirContext();

            // Create the DNS lookup attributes
            Attributes attributes = ctx.getAttributes(domain, new String[]{MX});
            Attribute attribute = attributes.get(MX);

            // Check if the MX record exists and is valid
            if (attribute != null && attribute.size() > 0) {
                String attr = attribute.toString();
                return convertAttributesToList(attr);
            }
        } catch (NamingException e) {
            // If a NamingException is thrown, the domain doesn't exist or there is no MX record
            return ERROR_LIST;
        }
        return ERROR_LIST;
    }

    private List<String> convertAttributesToList(String attributes) {
        return Arrays.stream(attributes.split(","))
                .map(row -> row.replace("MX:", StringUtils.EMPTY))
                .map(row -> row.replaceFirst(SPACE, StringUtils.EMPTY))
                .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.split(SPACE)[0])))
                .toList();
    }
}