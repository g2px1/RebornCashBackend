package com.client.eurekaclient.models.request.unit;

public class TransferTokensRequests {
    public String recipient;
    public String sender;
    public double amount;
    public String tokenName;

    public TransferTokensRequests() {
    }

    public TransferTokensRequests(String recipient, String sender, double amount, String tokenName) {
        this.recipient = recipient;
        this.sender = sender;
        this.amount = amount;
        this.tokenName = tokenName;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
