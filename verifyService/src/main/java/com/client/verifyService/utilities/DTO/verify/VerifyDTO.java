package com.client.verifyService.utilities.DTO.verify;

public class VerifyDTO {
    public String username;
    public Boolean authorized;

    public VerifyDTO(String username, Boolean authorized) {
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
