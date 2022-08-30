package com.client.verifyService.utilities.DTO.verify;

public class VerifyDTO {
    public String username;
    public boolean authorized;

    public VerifyDTO(String username, boolean authorized) {
        this.username = username;
        this.authorized = authorized;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
