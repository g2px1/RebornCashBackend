package com.client.eurekaclient.users;

public class Role {
  private String id;
  private ERole name;

  public Role() {}
  public Role(ERole name) {
    this.name = name;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public ERole getName() {
    return name;
  }
  public void setName(ERole name) {
    this.name = name;
  }
}
