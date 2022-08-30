package com.client.authorizationService.services.authorization;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import com.client.authorizationService.models.DTO.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private UserInterface userInterface;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userInterface.getUser(username);
        if (user.isEmpty()) throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        return UserDetailsImplementation.build(user.get());
    }
    public Optional<User> loadUser(String username) {
        return userInterface.getUser(username);
    }
}
