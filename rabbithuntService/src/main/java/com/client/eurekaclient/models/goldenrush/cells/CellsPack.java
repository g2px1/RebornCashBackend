package com.client.eurekaclient.models.goldenrush.cells;

import com.client.eurekaclient.models.goldenrush.mine.Mine;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cellsPacks")
public class CellsPack {
    @Id
    public String id;
    public long index;
    @DBRef
    public Mine mine;
    public String nftName;
    public BigDecimal price;
    public BigDecimal quantity;
    public String status;
    public String username;

    public CellsPack() {}

    public CellsPack(long index, Mine mine, String nftName, BigDecimal price) {
        this.index = index;
        this.mine = mine;
        this.nftName = nftName;
        this.price = price;
    }

    public CellsPack(long index, Mine mine, String nftName, BigDecimal price, BigDecimal quantity, String status) {
        this.index = index;
        this.mine = mine;
        this.nftName = nftName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public CellsPack(long index, Mine mine, String nftName, BigDecimal price, BigDecimal quantity, String status, String username) {
        this.index = index;
        this.mine = mine;
        this.nftName = nftName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Mine getTrap() {
        return mine;
    }

    public void setTrap(Mine mine) {
        this.mine = mine;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
