package com.client.eurekaclient.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class ErrorMessage {
    @Value("${app.error.message.PRODUCT_NOT_FOUND}")
    public static String PRODUCT_NOT_FOUND;
}
