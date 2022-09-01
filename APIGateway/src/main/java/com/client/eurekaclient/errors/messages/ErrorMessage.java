package com.client.eurekaclient.errors.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class ErrorMessage {
    @Value("${app.error.message.BEARER_REQUIRED}")
    public static String BEARER_REQUIRED;
    @Value("${app.error.message.TOKEN_EXPIRED}")
    public static String TOKEN_EXPIRED;

}
