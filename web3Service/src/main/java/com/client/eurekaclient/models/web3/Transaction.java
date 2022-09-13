package com.client.eurekaclient.models.web3;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class Transaction {
    @Id
    public String id;
    public String hash;
    public String chainName;
    public String username;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getChain() {
        return chainName;
    }

    public void setChain(String chainName) {
        this.chainName = chainName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
