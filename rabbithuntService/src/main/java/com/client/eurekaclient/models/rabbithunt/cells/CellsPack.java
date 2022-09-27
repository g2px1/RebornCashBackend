package com.client.eurekaclient.models.rabbithunt.cells;

import com.client.eurekaclient.models.rabbithunt.trap.Trap;
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
    public Trap trap;
    public String nftName;
    public BigDecimal price;
    public BigDecimal quantity;
    public String status;
    public String username;

    public CellsPack() {}

    public CellsPack(long index, Trap trap, String nftName, BigDecimal price) {
        this.index = index;
        this.trap = trap;
        this.nftName = nftName;
        this.price = price;
    }

    public CellsPack(long index, Trap trap, String nftName, BigDecimal price, BigDecimal quantity, String status) {
        this.index = index;
        this.trap = trap;
        this.nftName = nftName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public CellsPack(long index, Trap trap, String nftName, BigDecimal price, BigDecimal quantity, String status, String username) {
        this.index = index;
        this.trap = trap;
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

    public Trap getTrap() {
        return trap;
    }

    public void setTrap(Trap trap) {
        this.trap = trap;
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
