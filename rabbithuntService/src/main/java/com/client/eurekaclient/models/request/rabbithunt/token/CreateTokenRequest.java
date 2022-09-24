package com.client.eurekaclient.models.request.rabbithunt.token;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class CreateTokenRequest {
    public String name;
    public BigDecimal supply;
    public short type;

    public CreateTokenRequest(String name, BigDecimal supply, short type) {
        this.name = name;
        this.supply = supply;
        this.type = type;
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

    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return true;
        return false;
    }
}
