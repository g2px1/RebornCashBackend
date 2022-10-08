package com.client.eurekaclient.models.market.goldenrush;

import com.client.eurekaclient.models.market.AbstractProduct;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("cellsPack")
public class CellsPack extends AbstractProduct {
    public String nftName;
    public String mineName;

    public CellsPack(double price, String seller, double quantity, long publicationDate, Object characteristics, String description, boolean status, String uuid, String nftName, String mineName) {
        super(price, seller, quantity, publicationDate, characteristics, description, "cellsPack", status, uuid);
        this.nftName = nftName;
        this.mineName = mineName;
    }
}
