package com.client.eurekaclient.models.request.merchant;

import jakarta.validation.constraints.NotBlank;

public class BuyTokensFromMerchantRequest {
    public double amount;
    @NotBlank
    public String tokenName;
    @NotBlank
    public String code;
    public long nftId;
    @NotBlank
    public String chainName;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNftId() {
        return nftId;
    }

    public void setNftId(long nftId) {
        this.nftId = nftId;
    }
}
