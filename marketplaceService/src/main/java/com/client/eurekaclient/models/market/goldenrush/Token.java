package com.client.eurekaclient.models.market.goldenrush;

import com.client.eurekaclient.models.market.AbstractProduct;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("token")
public class Token extends AbstractProduct {
    public String nftName;
    public String tokenName;

    public Token(double price, String seller, double quantity, long publicationDate, Object characteristics, String description, boolean status, String uuid, String nftName, String tokenName) {
        super(price, seller, quantity, publicationDate, characteristics, description, "token", status, uuid);
        this.nftName = nftName;
        this.tokenName = tokenName;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
