package com.client.eurekaclient.models.request.goldenrush.token;

public class TokenSeekingRequest {
    public String tokenType;
    public int page;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
