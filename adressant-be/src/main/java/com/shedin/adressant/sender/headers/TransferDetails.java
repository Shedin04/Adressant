package com.shedin.adressant.sender.headers;

import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Builder
public class TransferDetails {

    private List<TransferPoint> transferPoints;
    private String totalMessageDelay;

    public static class TransferDetailsBuilder {
        public TransferDetails build() {
            if (Stream.of(this.transferPoints, this.totalMessageDelay)
                    .allMatch(Objects::isNull)) {
                return null;
            } else {
                return new TransferDetails(this.transferPoints, this.totalMessageDelay);
            }
        }
    }
}
