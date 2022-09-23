package com.client.eurekaclient.models.scheduled.transactions;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "layer1ExpiringTransactions")
public class ScheduledTransaction {
    @Id
    public String id;
    public long activeTill = new Date().getTime();
    public String unitHash;
    public long nftIndex;
    public double amount;
    public String tokenName;
    public boolean reverted = false;

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

    public long getNftIndex() {
        return nftIndex;
    }

    public void setNftIndex(long nftIndex) {
        this.nftIndex = nftIndex;
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
