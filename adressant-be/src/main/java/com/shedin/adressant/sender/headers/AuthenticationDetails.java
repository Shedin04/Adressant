package com.shedin.adressant.sender.headers;

import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

@Builder
public class AuthenticationDetails {

    private String authenticationResults;
    private String arcAuthenticationResults;
    private String arcSeal;
    private String arcMessageSignature;
    private String receivedSpf;
    private String dkimSignature;

    public static class AuthenticationDetailsBuilder {
        public AuthenticationDetails build() {
            if (Stream.of(this.authenticationResults, this.arcAuthenticationResults, this.arcSeal, this.arcMessageSignature, this.receivedSpf, this.dkimSignature)
                    .allMatch(Objects::isNull)) {
                return null;
            } else {
                return new AuthenticationDetails(this.authenticationResults, this.arcAuthenticationResults, this.arcSeal, this.arcMessageSignature, this.receivedSpf, this.dkimSignature);
            }
        }
    }
}
