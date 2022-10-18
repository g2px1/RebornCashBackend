package com.client.userService.errors.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Errors {
    @Value("${app.error.message.USER_EXISTS}") public String USER_EXISTS;
    @Value("${app.error.message.USER_NOT_FOUND}") public String USER_NOT_FOUND;
    @Value("${app.error.message.USER_BANNED}") public String USER_BANNED;
    @Value("${app.error.message.DEFAULT}") public String DEFAULT_ERROR;
    @Value("${app.error.message.NEED_TO_PASS_2FA}") public String NEED_TO_PASS_2FA;
}
