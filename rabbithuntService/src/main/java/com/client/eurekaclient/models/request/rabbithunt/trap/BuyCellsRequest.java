package com.client.eurekaclient.models.request.rabbithunt.trap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BuyCellsRequest {
    public String trapName;
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "error occurred in quantity")
    public int quantity;
    public long nftIndex;
    public String code;
    public String chainName;

    public String getTrapName() {
        return trapName;
    }

    public void setTrapName(String trapName) {
        this.trapName = trapName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getNftIndex() {
        return nftIndex;
    }

    public void setNftIndex(long nftIndex) {
        this.nftIndex = nftIndex;
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
