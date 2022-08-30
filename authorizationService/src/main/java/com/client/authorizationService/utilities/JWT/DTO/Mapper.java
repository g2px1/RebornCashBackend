package com.client.authorizationService.utilities.JWT.DTO;

import com.client.authorizationService.users.User;
import com.client.authorizationService.utilities.JWT.DTO.user.NonSecureUserDTO;
import com.client.authorizationService.utilities.JWT.DTO.user.SecureUserDTO;
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
