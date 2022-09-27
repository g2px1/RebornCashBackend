package com.client.eurekaclient.models.rabbithunt.trap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "CellsTransaction")
public class CellsTransactions {
    @Id
    public String id;
    @DBRef
    public Trap trap;
    public String nftName;
    public String nftUuid;
    public long Date = new Date().getTime();
    public int quantity;
    public String outHash;
    public String inHash;

    public CellsTransactions(Trap trap, String nftName, int quantity) {
        this.trap = trap;
        this.nftName = nftName;
        this.quantity = quantity;
    }

    public CellsTransactions(Trap trap, String nftName, int quantity, String outHash, String inHash) {
        this.trap = trap;
        this.nftName = nftName;
        this.quantity = quantity;
        this.outHash = outHash;
        this.inHash = inHash;
    }

    public CellsTransactions(Trap trap, String nftName, int quantity, String outHash, String inHash, String nftUuid) {
        this.trap = trap;
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
