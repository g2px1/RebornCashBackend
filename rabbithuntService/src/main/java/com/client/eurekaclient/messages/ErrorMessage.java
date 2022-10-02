package com.client.eurekaclient.messages;

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
    @Value("${app.error.message.NEED_TO_PASS_2FA}")
    public static String NEED_TO_PASS_2FA;
    @Value("${app.error.message.NEED_TO_BE_2FA}")
    public static String NEED_TO_BE_2FA;
    @Value("${app.error.message.CHAIN_NOT_SUPPORTED}")
    public static String CHAIN_NOT_SUPPORTED;
    @Value("${app.error.message.TRANSACTION_ERROR}")
    public static String TRANSACTION_ERROR;
    @Value("${app.error.message.LOW_GAME_BALANCE}")
    public static String LOW_GAME_BALANCE;
    @Value("${app.error.message.LOW_CARROT_BALANCE}")
    public static String LOW_CARROT_BALANCE;
    @Value("${app.error.message.LOW_MEAT_BALANCE}")
    public static String LOW_MEAT_BALANCE;
    @Value("${app.error.message.LOW_TOKEN_BALANCE}")
    public static String LOW_TOKEN_BALANCE;
    @Value("${app.error.message.BALANCE_NOT_FOUND}")
    public static String BALANCE_NOT_FOUND;
    @Value("${app.error.message.TRAP_NOT_EXIST}")
    public static String TRAP_NOT_EXIST;
    @Value("${app.error.message.INVALID_CODE}")
    public static String INVALID_CODE;
    @Value("${app.error.message.TRAP_EXPIRED}")
    public static String TRAP_EXPIRED;
    @Value("${app.error.message.METAMASK_ERROR}")
    public static String METAMASK_ERROR;
    @Value("${app.error.message.NFT_NOT_EXISTS}")
    public static String NFT_NOT_EXISTS;
    @Value("${app.error.message.OWNERSHIP_ERROR}")
    public static String OWNERSHIP_ERROR;
    @Value("${app.error.message.INVALID_DATA}")
    public static String INVALID_DATA;
    @Value("${app.error.message.TRAP_OUT_OF_EMISSION}")
    public static String TRAP_OUT_OF_EMISSION;
    @Value("${app.error.message.NOT_ENOUGH_CARROTS}")
    public static String NOT_ENOUGH_CARROTS;
    @Value("${app.error.message.LOCK}")
    public static String LOCK;
    @Value("${app.error.message.TRAP_CLOSED}")
    public static String TRAP_CLOSED;
    @Value("${app.error.message.TRAP_IS_FULL}")
    public static String TRAP_IS_FULL;
    @Value("${app.error.message.BURGER_QUANTITY_ERROR}")
    public static String BURGER_QUANTITY_ERROR;
    @Value("${app.error.message.TOKEN_NOT_EXISTS}")
    public static String TOKEN_NOT_EXISTS;
    @Value("${app.error.message.API_NOT_AVAILABLE}")
    public static String API_NOT_AVAILABLE;
    @Value("${app.error.message.ALL_FIELDS_SHOULD_BE_FILLED_IN}")
    public static String ALL_FIELDS_SHOULD_BE_FILLED_IN;
    @Value("${app.error.message.AMOUNT_IS_TOO_LARGE}")
    public static String AMOUNT_IS_TOO_LARGE;
    @Value("${app.error.message.PRODUCT_NOT_FOUND}")
    public static String PRODUCT_NOT_FOUND;
    @Value("${app.error.message.CELLSPACK_NOT_FOUND}")
    public static String CELLSPACK_NOT_FOUND;
}
