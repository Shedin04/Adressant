package com.shedin.adressant.sender.headers;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailHeaders {
    public static final String FROM = "From";
    public static final String RESENT_FROM = "Resent-From";
    public static final String SENDER = "Sender";
    public static final String RESENT_SENDER = "Resent-Sender";
    public static final String REPLY_TO = "Reply-To";
    public static final String TO = "To";
    public static final String DELIVERED_TO = "Delivered-To";
    public static final String IN_REPLY_TO = "In-Reply-To";
    public static final String RESENT_TO = "Resent-To";
    public static final String RETURN_PATH = "Return-Path";
    public static final String DISPOSITION_NOTIFICATION_TO = "Disposition-Notification-To";
    public static final String REFERENCES = "References";
    public static final String CC = "Cc";
    public static final String BCC = "Bcc";
    public static final String RESENT_CC = "Resent-Cc";
    public static final String RESENT_BCC = "Resent-Bcc";
    public static final String SUBJECT = "Subject";
    public static final String MESSAGE_ID = "Message-ID";
    public static final String DATE = "Date";
    public static final String MESSAGE_CONTEXT = "Message-Context";
    public static final String MESSAGE_TYPE = "Message-Type";
    public static final String ORIGINAL_MESSAGE_ID = "Original-Message-ID";
    public static final String RESENT_MESSAGE_ID = "Resent-Message-ID";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TRANSLATION_TYPE = "Content-Translation-Type";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String CONTENT_ID = "Content-ID";
    public static final String CONTENT_IDENTIFIER = "Content-Identifier";
    public static final String CONTENT_DESCRIPTION = "Content-Description";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_BASE = "Content-Base";
    public static final String CONTENT_RETURN = "Content-Return";
    public static final String CONTENT_MD5 = "Content-MD5";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_FEATURES = "Content-features";
    public static final String CONTENT_DURATION = "Content-Duration";
    public static final String CONTENT_ALTERNATIVE = "Content-Alternative";
    public static final String MIME_VERSION = "MIME-Version";
    public static final String RECEIVED = "Received";
    public static final String X_RECEIVED = "X-Received";
    public static final String AUTHENTICATION_RESULTS = "Authentication-Results";
    public static final String ARC_AUTHENTICATION_RESULTS = "ARC-Authentication-Results";
    public static final String ARC_SEAL = "ARC-Seal";
    public static final String ARC_MESSAGE_SIGNATURE = "ARC-Message-Signature";
    public static final String RECEIVED_SPF = "Received-SPF";
    public static final String DKIM_SIGNATURE = "DKIM-Signature";

}