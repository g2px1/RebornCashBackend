package com.client.eurekaclient.models.request.goldenrush.mine;


import jakarta.validation.constraints.NotBlank;

public class CloseMineRequest {
    @NotBlank
    public String mineName;
    public String price;

    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
