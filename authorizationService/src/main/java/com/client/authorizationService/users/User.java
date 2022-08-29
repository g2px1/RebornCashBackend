package com.client.authorizationService.users;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
  private String id;
  @NotBlank
  @Size(max = 20)
  private String username;
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
  private long userNumber;
  @NotBlank
  @Size(max = 120)
  private String password;
  private BigDecimal balance = BigDecimal.valueOf(0);
  private long openedBoxes = 0;
  private boolean twoFA = false;
  private String status = "active";
  private List<Role> roles = new ArrayList<>();
  private String secretKey = TimeBasedOneTimePasswordUtil.generateBase32Secret();
  private int nonce;

  public User() {}
  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
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
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public List<Role> getRoles() {
    return roles;
  }
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }
  public BigDecimal getBalance() {
    return balance;
  }
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
  public String getSecretKey() {
    return secretKey;
  }
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
  public boolean isTwoFA() {
    return twoFA;
  }
  public void setTwoFA(boolean twoFA) {
    this.twoFA = twoFA;
  }
  public long getOpenedBoxes() {
    return openedBoxes;
  }
  public void setOpenedBoxes(long openedBoxes) {
    this.openedBoxes = openedBoxes;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public long getUserNumber() {
    return userNumber;
  }
  public void setUserNumber(long userNumber) {
    this.userNumber = userNumber;
  }
  public int getNonce() {
    return nonce;
  }
  public void setNonce(int nonce) {
    this.nonce = nonce;
  }
  @Override
  public String toString() {
    return String.format("{id=%s, username=%s, email=%s, userNumber=%s, password=%s, balance=%s, twoFA=%s, status=%s, secretKey=%s}", this.id, this.username, this.email, this.userNumber, this.password, this.balance, this.twoFA, this.status, this.secretKey);
  }
}
