package com.client.eurekaclient.models.rabbithunt.tokens;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "rabbitHuntTokens")
public class Token {
    @Id
    public String id; // mongo id
    public String address = ""; // address in Unit chain
    public String name; // token name(e.g carrots, meat etc)
    public BigDecimal supply; // emission of token in UnitChain
    public short type; // int or double. Int is 0 type, Double is 1 type
    public String tokenType;

    public Token() {}

    public Token(String id, String address, String name, BigDecimal supply, short type) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.supply = supply;
        this.type = type;
    }

    public Token(String id, String name, BigDecimal supply, short type) {
        this.id = id;
        this.name = name;
        this.supply = supply;
        this.type = type;
    }

    public Token(String name, BigDecimal supply, short type) {
        this.name = name;
        this.supply = supply;
        this.type = type;
    }

    public Token(String name, BigDecimal supply, short type, String tokenType) {
        this.name = name;
        this.supply = supply;
        this.type = type;
        this.tokenType = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSupply() {
        return supply;
    }

    public void setSupply(BigDecimal supply) {
        this.supply = supply;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
