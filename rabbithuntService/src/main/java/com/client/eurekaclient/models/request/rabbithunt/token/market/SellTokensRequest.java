package com.client.eurekaclient.models.request.rabbithunt.token.market;

import jakarta.validation.constraints.NotBlank;

public class SellTokensRequest {
    public double price;
    public double amount;
    @NotBlank
    public String tokenName;
    public long NFTId;
    @NotBlank
    public String code;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public long getNFTId() {
        return NFTId;
    }

    public void setNFTId(long NFTId) {
        this.NFTId = NFTId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
