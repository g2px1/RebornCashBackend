package com.client.userService.utilities.DTO;

import com.client.userService.models.dbModels.users.User;
import com.client.userService.utilities.DTO.user.NonSecureUserDTO;
import com.client.userService.utilities.DTO.user.SecureUserDTO;
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
