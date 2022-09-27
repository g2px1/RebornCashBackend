package com.client.eurekaclient.models.scheduled.transactions;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "scheduledTransactions")
public class ScheduledTransaction {
    @Id
    public String id;
    public long activeTill = new Date().getTime();
    public String unitHash;
    public String nftName;
    public double amount;
    public String tokenName;
    public boolean reverted = false;

    public ScheduledTransaction() {
    }

    public ScheduledTransaction(long activeTill, String unitHash, String nftName, double amount, String tokenName, boolean reverted) {
        this.activeTill = activeTill;
        this.unitHash = unitHash;
        this.nftName = nftName;
        this.amount = amount;
        this.tokenName = tokenName;
        this.reverted = reverted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(long activeTill) {
        this.activeTill = activeTill;
    }

    public String getUnitHash() {
        return unitHash;
    }

    public void setUnitHash(String unitHash) {
        this.unitHash = unitHash;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public boolean isReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }
}
