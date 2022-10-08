package com.client.eurekaclient.models.market;

import com.client.eurekaclient.models.market.goldenrush.CellsPack;
import com.client.eurekaclient.models.market.goldenrush.Token;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CellsPack.class, name = "cellsPack"),
        @JsonSubTypes.Type(value = Token.class, name = "token")
})
public class AbstractProduct implements Serializable {
    @Id
    private String id;
    public double price;
    public String seller;
    public double quantity;
    public long publicationDate;
    public Object characteristics;
    public String description;
    public String type;
    public boolean status;
    public String uuid = UUID.randomUUID().toString();

    public AbstractProduct(double price, String seller, double quantity, long publicationDate, Object characteristics, String description, String type, boolean status, String uuid) {
        this.price = price;
        this.seller = seller;
        this.quantity = quantity;
        this.publicationDate = publicationDate;
        this.characteristics = characteristics;
        this.description = description;
        this.type = type;
        this.status = status;
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(long publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Object getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Object characteristics) {
        this.characteristics = characteristics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
