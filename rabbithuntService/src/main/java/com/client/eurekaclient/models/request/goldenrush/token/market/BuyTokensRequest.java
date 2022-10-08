package com.client.eurekaclient.models.request.goldenrush.token.market;


import jakarta.validation.constraints.NotBlank;

public class BuyTokensRequest {
    @NotBlank
    public String uuid;
    @NotBlank
    public String code;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
