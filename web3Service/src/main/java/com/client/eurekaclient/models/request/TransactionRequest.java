package com.client.eurekaclient.models.request;

import java.math.BigDecimal;

public class TransactionRequest {
    public String address;
    public BigDecimal amount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
