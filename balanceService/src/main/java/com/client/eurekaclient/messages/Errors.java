package com.client.eurekaclient.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Errors {
    @Value("${app.error.message.USER_EXISTS}") public String USER_EXISTS;
    @Value("${app.error.message.USER_NOT_FOUND}") public String USER_NOT_FOUND;
    @Value("${app.error.message.USER_BANNED}") public String USER_BANNED;
    @Value("${app.error.message.DEFAULT}") public String DEFAULT_ERROR;
    @Value("${app.error.message.BOT}") public String BOT;
    @Value("${app.error.message.NEED_TO_PASS_2FA}") public String NEED_TO_PASS_2FA;
    @Value("${app.error.message.CHAIN_NOT_SUPPORTED}") public String CHAIN_NOT_SUPPORTED;
    @Value("${app.error.message.TRANSACTION_ERROR}") public String TRANSACTION_ERROR;
    @Value("${app.error.message.LOW_GAME_BALANCE}") public String LOW_NATIVE_TOKENS_BALANCE;
    @Value("${app.error.message.INVALID_CODE}") public String INVALID_CODE;
    @Value("${app.error.message.METAMASK_ERROR}") public String METAMASK_ERROR;
    @Value("${app.error.message.LOCK}") public String LOCK;
    @Value("${app.error.message.INVALID_RECIPIENT}") public String INVALID_RECIPIENT;
    @Value("${app.error.message.BLOCKCHAIN_ALREADY_ADDED}") public String BLOCKCHAIN_ALREADY_ADDED;
}
