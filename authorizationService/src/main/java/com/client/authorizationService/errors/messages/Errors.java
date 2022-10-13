package com.client.authorizationService.errors.messages;

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
    @Value("${app.error.message.NEED_TO_BE_2FA}") public String NEED_TO_BE_2FA;
    @Value("${app.error.message.CHAIN_NOT_SUPPORTED}") public String CHAIN_NOT_SUPPORTED;
    @Value("${app.error.message.TRANSACTION_ERROR}") public String TRANSACTION_ERROR;
    @Value("${app.error.message.LOW_GAME_BALANCE}") public String LOW_NATIVE_TOKENS_BALANCE;
    @Value("${app.error.message.LOW_CARROT_BALANCE}") public String LOW_CARROT_BALANCE;
    @Value("${app.error.message.LOW_GOLD_COINS_BALANCE}") public String LOW_GOLD_COINS_BALANCE;
    @Value("${app.error.message.LOW_TOKEN_BALANCE}") public String LOW_TOKEN_BALANCE;
    @Value("${app.error.message.BALANCE_NOT_FOUND}") public String BALANCE_NOT_FOUND;
    @Value("${app.error.message.MINE_NOT_EXIST}") public String MINE_NOT_EXIST;
    @Value("${app.error.message.INVALID_CODE}") public String INVALID_CODE;
    @Value("${app.error.message.MINE_EXPIRED}") public String MINE_EXPIRED;
    @Value("${app.error.message.METAMASK_ERROR}") public String METAMASK_ERROR;
    @Value("${app.error.message.NFT_NOT_EXISTS}") public String NFT_NOT_EXISTS;
    @Value("${app.error.message.OWNERSHIP_ERROR}") public String OWNERSHIP_ERROR;
    @Value("${app.error.message.INVALID_DATA}") public String INVALID_DATA;
    @Value("${app.error.message.MINE_OUT_OF_EMISSION}") public String MINE_OUT_OF_EMISSION;
    @Value("${app.error.message.NOT_ENOUGH_CARROTS}") public String NOT_ENOUGH_CARROTS;
    @Value("${app.error.message.LOCK}") public String LOCK;
    @Value("${app.error.message.MINE_CLOSED}") public String MINE_CLOSED;
    @Value("${app.error.message.MINE_IS_FULL}") public String MINE_IS_FULL;
    @Value("${app.error.message.BURGER_QUANTITY_ERROR}") public String BURGER_QUANTITY_ERROR;
    @Value("${app.error.message.TOKEN_NOT_EXISTS}") public String TOKEN_NOT_EXISTS;
    @Value("${app.error.message.API_NOT_AVAILABLE}") public String API_NOT_AVAILABLE;
    @Value("${app.error.message.ALL_FIELDS_SHOULD_BE_FILLED_IN}") public String ALL_FIELDS_SHOULD_BE_FILLED_IN;
    @Value("${app.error.message.AMOUNT_IS_TOO_LARGE}") public String AMOUNT_IS_TOO_LARGE;
    @Value("${app.error.message.PRODUCT_NOT_FOUND}") public String PRODUCT_NOT_FOUND;
    @Value("${app.error.message.CELLSPACK_NOT_FOUND}") public String CELLSPACK_NOT_FOUND;
}
