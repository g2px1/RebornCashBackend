package com.client.eurekaclient.models.DTO.authorization;

public class AuthorizationDTO {
    public String username;
    public String password;
    public String email;
    public String captchaToken;
    public String code;

    public AuthorizationDTO(String username, String password, String email, String captchaToken, String code) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.captchaToken = captchaToken;
        this.code = code;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCaptchaToken() {
        return captchaToken;
    }
    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
