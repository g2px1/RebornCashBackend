package com.client.userService.services.authorization;

import com.client.userService.errors.messages.Errors;
import com.client.userService.messages.ErrorMessage;
import com.client.userService.models.DTO.users.User;
import com.client.userService.services.openfeign.users.UserInterface;
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
    @Autowired
    private Errors errors;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userInterface.getUser(username);
        if (user.isEmpty()) throw new UsernameNotFoundException(errors.USER_NOT_FOUND);
        return UserDetailsImplementation.build(user.get());
    }
    public Optional<User> loadUser(String username) {
        return userInterface.getUser(username);
    }
}
