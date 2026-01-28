package com.shedin.adressant.sender.headers;

import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

@Builder
public class ContentDetails {

    private String contentType;
    private String contentTranslationType;
    private String contentDisposition;
    private String contentTransferEncoding;
    private String contentID;
    private String contentIdentifier;
    private String contentDescription;
    private String contentLanguage;
    private String contentBase;
    private String contentReturn;
    private String contentMD5;
    private String contentLocation;
    private String contentFeatures;
    private String contentDuration;
    private String contentAlternative;
    private String mimeVersion;

    public static class ContentDetailsBuilder {
        public ContentDetails build() {
            if (Stream.of(this.contentType, this.contentTranslationType, this.contentDisposition, this.contentTransferEncoding, this.contentID, this.contentIdentifier, this.contentDescription, this.contentLanguage, this.contentBase, this.contentReturn, this.contentMD5, this.contentLocation, this.contentFeatures, this.contentDuration, this.contentAlternative, this.mimeVersion)
                    .allMatch(Objects::isNull)) {
                return null;
            } else {
                return new ContentDetails(this.contentType, this.contentTranslationType, this.contentDisposition, this.contentTransferEncoding, this.contentID, this.contentIdentifier, this.contentDescription, this.contentLanguage, this.contentBase, this.contentReturn, this.contentMD5, this.contentLocation, this.contentFeatures, this.contentDuration, this.contentAlternative, this.mimeVersion);
            }
        }
    }
}
