package com.client.eurekaclient.models.goldenrush.mine;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "CellsTransaction")
public class CellsTransactions {
    @Id
    public String id;
    @DBRef
    public Mine mine;
    public String nftName;
    public String nftUuid;
    public long Date = new Date().getTime();
    public int quantity;
    public String outHash;
    public String inHash;

    public CellsTransactions() {
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public CellsTransactions(Mine mine, String nftName, int quantity) {
        this.mine = mine;
        this.nftName = nftName;
        this.quantity = quantity;
    }

    public CellsTransactions(Mine mine, String nftName, int quantity, String outHash, String inHash) {
        this.mine = mine;
        this.nftName = nftName;
        this.quantity = quantity;
        this.outHash = outHash;
        this.inHash = inHash;
    }

    public CellsTransactions(Mine mine, String nftName, int quantity, String outHash, String inHash, String nftUuid) {
        this.mine = mine;
        this.nftName = nftName;
        this.quantity = quantity;
        this.outHash = outHash;
        this.inHash = inHash;
        this.nftUuid = nftUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNftUuid() {
        return nftUuid;
    }

    public void setNftUuid(String nftUuid) {
        this.nftUuid = nftUuid;
    }

    public String getOutHash() {
        return outHash;
    }

    public void setOutHash(String outHash) {
        this.outHash = outHash;
    }

    public String getInHash() {
        return inHash;
    }

    public void setInHash(String inHash) {
        this.inHash = inHash;
    }
}
