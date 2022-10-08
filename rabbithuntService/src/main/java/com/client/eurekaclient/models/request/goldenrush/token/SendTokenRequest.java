package com.client.eurekaclient.models.request.goldenrush.token;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class SendTokenRequest {
    public String recipient;
    public BigDecimal amount;
    public String tokenName;

    public SendTokenRequest(String recipient, BigDecimal amount, String tokenName) {
        this.recipient = recipient;
        this.amount = amount;
        this.tokenName = tokenName;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return true;
        return false;
    }
}
