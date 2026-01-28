package com.shedin.adressant.sender.headers;

import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

@Builder
public class AddressDetails {

    private String mailFrom;
    private String mailFromName;
    private String resentFrom;
    private String sender;
    private String resentSender;
    private String mailTo;
    private String deliveredTo;
    private String replyTo;
    private String resentTo;
    private String returnPath;
    private String dispositionNotificationTo;
    private String carbonCopy;
    private String blindCarbonCopy;
    private String resentCarbonCopy;
    private String resentBlindCarbonCopy;

    public static class AddressDetailsBuilder {
        public AddressDetails build() {
            if (Stream.of(this.mailFrom, this.mailFromName, this.resentFrom, this.sender, this.resentSender, this.mailTo, this.deliveredTo, this.replyTo, this.resentTo, this.returnPath, this.dispositionNotificationTo, this.carbonCopy, this.blindCarbonCopy, this.resentCarbonCopy, this.resentBlindCarbonCopy)
                    .allMatch(Objects::isNull)) {
                return null;
            } else {
                return new AddressDetails(this.mailFrom, this.mailFromName, this.resentFrom, this.sender, this.resentSender, this.mailTo, this.deliveredTo, this.replyTo, this.resentTo, this.returnPath, this.dispositionNotificationTo, this.carbonCopy, this.blindCarbonCopy, this.resentCarbonCopy, this.resentBlindCarbonCopy);
            }
        }
    }
}
