package com.client.verifyService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verify")
public class Verify2FA {
    @Id
    public String id;
    public String username;
    public boolean isAuthorized;

    public Verify2FA(String username, boolean isAuthorized) {
        this.username = username;
        this.isAuthorized = isAuthorized;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isAuthorized() {
        return isAuthorized;
    }
    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
}
