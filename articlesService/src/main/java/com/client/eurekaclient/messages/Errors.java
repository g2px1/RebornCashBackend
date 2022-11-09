package com.client.eurekaclient.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Errors {
    @Value("${app.error.message.NFT_NOT_FOUND}") public String NFT_NOT_FOUND;
    @Value("${app.error.message.USER_EXISTS}") public String USER_EXISTS;
    @Value("${app.error.message.USER_NOT_FOUND}") public String USER_NOT_FOUND;
    @Value("${app.error.message.USER_BANNED}") public String USER_BANNED;
    @Value("${app.error.message.DEFAULT}") public String DEFAULT_ERROR;
    @Value("${app.error.message.NEED_TO_PASS_2FA}") public String NEED_TO_PASS_2FA;
    @Value("${app.error.message.ARTICLE_EXISTS}") public String ARTICLE_EXISTS;
    @Value("${app.error.message.ARTICLE_NOT_EXISTS}") public String ARTICLE_NOT_EXISTS;
}
