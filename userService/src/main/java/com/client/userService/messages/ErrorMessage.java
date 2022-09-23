package com.client.userService.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class ErrorMessage {
    @Value("${app.error.message.NFT_NOT_FOUND}")
    public static String NFT_NOT_FOUND;
    @Value("${app.error.message.USER_EXISTS}")
    public static String USER_EXISTS;
    @Value("${app.error.message.USER_NOT_FOUND}")
    public static String USER_NOT_FOUND;
    @Value("${app.error.message.USER_BANNED}")
    public static String USER_BANNED;
    @Value("${app.error.message.DEFAULT}")
    public static String DEFAULT_ERROR;
    @Value("${app.error.message.NEED_TO_PASS_2FA}")
    public static String NEED_TO_PASS_2FA;
    @Value("${app.error.message.ARTICLE_EXISTS}")
    public static String ARTICLE_EXISTS;
    @Value("${app.error.message.ARTICLE_NOT_EXISTS}")
    public static String ARTICLE_NOT_EXISTS;
}
