package com.shedin.adressant.sender.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Log4j2
@NoArgsConstructor
public class EmailChecker {

    @Value("${temp.mails.path}")
    private Resource tempMailsFile;
    @Value("${free.mails.path}")
    private Resource freeMailsFile;

    public boolean isTemporary(String emailDomain) {
        Set<String> emails = getEmailsList(tempMailsFile);
        return emails.stream().anyMatch(emailDomain::equalsIgnoreCase);
    }

    public boolean isFree(String emailDomain) {
        Set<String> emails = getEmailsList(freeMailsFile);
        return emails.stream().anyMatch(emailDomain::equalsIgnoreCase);
    }

    private JsonReader getReader(Resource file) {
        try {
            return new JsonReader(new InputStreamReader(Objects.requireNonNull(file.getInputStream())));
        } catch (IOException e) {
            log.error("Error appeared when creating reader for file '{}' | '{}'", file.getFilename(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Set<String> getEmailsList(Resource file) {
        Type listType = new TypeToken<HashSet<String>>() {
        }.getType();
        return new Gson().fromJson(getReader(file), listType);
    }
}
