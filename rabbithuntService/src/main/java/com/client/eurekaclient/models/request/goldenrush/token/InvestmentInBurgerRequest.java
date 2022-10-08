package com.client.eurekaclient.models.request.goldenrush.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class InvestmentInBurgerRequest {
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "error occurred in quantity")
    public long quantityOfBurgers;
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "error occurred in index")
    public long index;
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "error occurred in code")
    public String code;
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "error occurred in code")
    public String chainName;

    public long getQuantityOfBurgers() {
        return quantityOfBurgers;
    }

    public void setQuantityOfBurgers(long quantityOfBurgers) {
        this.quantityOfBurgers = quantityOfBurgers;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
