package com.shedin.adressant.sender.headers;

import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

@Builder
public class MessageDetails {

    private String subject;
    private String messageId;
    private String date;
    private String dateInUtc;
    private String messageContext;
    private String messageType;
    private String originalMessageId;
    private String resentMessageId;
    private String inReplyTo;
    private String references;

    public static class MessageDetailsBuilder {
        public MessageDetails build() {
            if (Stream.of(this.subject, this.messageId, this.date, this.dateInUtc, this.messageContext, this.messageType, this.originalMessageId, this.resentMessageId, this.inReplyTo, this.references)
                    .allMatch(Objects::isNull)) {
                return null;
            } else {
                return new MessageDetails(this.subject, this.messageId, this.date, this.dateInUtc, this.messageContext, this.messageType, this.originalMessageId, this.resentMessageId, this.inReplyTo, this.references);
            }
        }
    }
}