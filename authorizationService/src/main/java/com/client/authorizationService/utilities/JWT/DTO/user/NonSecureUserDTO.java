package com.client.authorizationService.utilities.JWT.DTO.user;

import com.client.authorizationService.users.User;

import java.math.BigDecimal;

public class NonSecureUserDTO {
    public String username;
    public String status;
    public BigDecimal balance;
    public boolean twoFa;
    public int nonce;

    public NonSecureUserDTO() {}
    public NonSecureUserDTO(User user) {
        this.username = user.getUsername();
        this.status = user.getStatus();
        this.balance = user.getBalance();
        this.twoFa = user.isTwoFA();
        this.nonce = user.getNonce();
    }
}
