package com.client.userService.utilities.DTO.user;

import com.client.userService.models.DTO.users.Role;
import com.client.userService.models.DTO.users.User;

import java.util.List;

public class SecureUserDTO {
    public String username;
    public String password;
    public String secretKey;
    public String status;
    public List<Role> roles;

    public SecureUserDTO() {}
    public SecureUserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.secretKey = user.getSecretKey();
        this.status = user.getStatus();
        this.roles = user.getRoles();
    }
}
