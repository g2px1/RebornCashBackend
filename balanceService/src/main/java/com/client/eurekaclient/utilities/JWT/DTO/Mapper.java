package com.client.eurekaclient.utilities.JWT.DTO;

import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.utilities.JWT.DTO.user.NonSecureUserDTO;
import com.client.eurekaclient.utilities.JWT.DTO.user.SecureUserDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public SecureUserDTO toSecureUserDTO(User user) {
        return new SecureUserDTO(user);
    }
    public NonSecureUserDTO toNonSecureUserDTO(User user) {
        return new NonSecureUserDTO(user);
    }
}
