package com.client.eurekaclient.messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Errors {
    @Value("${app.error.message.PRODUCT_NOT_FOUND}") public String PRODUCT_NOT_FOUND;
}
