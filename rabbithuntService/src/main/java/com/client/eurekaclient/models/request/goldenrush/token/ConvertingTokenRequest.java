package com.client.eurekaclient.models.request.goldenrush.token;

public class ConvertingTokenRequest {
    public long nftIndex;
    public double tokenAmount;
    public String tokenName;
    public String code;
    public String chainName;

    public long getNftIndex() {
        return nftIndex;
    }

    public void setNftIndex(long nftIndex) {
        this.nftIndex = nftIndex;
    }

    public double getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(double tokenAmount) {
        this.tokenAmount = tokenAmount;
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

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }
}
