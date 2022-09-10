package com.client.eurekaclient.errors.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class ErrorMessage {
    @Value("${app.error.message.USER_EXISTS}")
    public static String USER_EXISTS;
    @Value("${app.error.message.USER_NOT_FOUND}")
    public static String USER_NOT_FOUND;
    @Value("${app.error.message.USER_BANNED}")
    public static String USER_BANNED;
    @Value("${app.error.message.DEFAULT}")
    public static String DEFAULT_ERROR;
    @Value("${app.error.message.BOT}")
    public static String BOT;
}
