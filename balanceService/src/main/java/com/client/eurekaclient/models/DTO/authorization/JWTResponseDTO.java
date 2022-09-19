package com.client.eurekaclient.models.DTO.authorization;

public class JWTResponseDTO {
    public String username;
    public boolean twoFA;
    public int nonce;
    public String token;
    private String type = "Bearer";

    public JWTResponseDTO(String token, String username, boolean twoFA, int nonce) {
        this.token = token;
        this.username = username;
        this.twoFA = twoFA;
        this.nonce = nonce;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isTwoFA() {
        return twoFA;
    }

    public void setTwoFA(boolean twoFA) {
        this.twoFA = twoFA;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }
}
