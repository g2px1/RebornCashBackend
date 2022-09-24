package com.client.eurekaclient.models.request.rabbithunt.trap;


import jakarta.validation.constraints.NotBlank;

public class CloseTrapRequest {
    @NotBlank
    public String trapName;
    public String price;

    public String getTrapName() {
        return trapName;
    }

    public void setTrapName(String trapName) {
        this.trapName = trapName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
