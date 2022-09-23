package com.client.userService.models.response;

import com.client.userService.models.DTO.users.Role;
import com.client.userService.models.DTO.users.User;

import java.math.BigDecimal;
import java.util.List;

public class UserResponse {
    public String id;
    public String username;
    public String email;
    public BigDecimal balance;
    public String status;
    public List<Role> roles;

    public UserResponse(String username, String email, BigDecimal balance, String status, List<Role> roles, String id) {
        this.username = username;
        this.email = email;
        this.balance = balance;
        this.status = status;
        this.roles = roles;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public static UserResponse build(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(), user.getBalance(), user.getStatus(), user.getRoles(), user.getId());
    }
}
