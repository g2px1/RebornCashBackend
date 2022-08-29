package com.client.authorizationService.services.authorization;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import com.client.authorizationService.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserDetailsServiceImplementation implements ReactiveUserDetailsService {
    @Autowired
    private UserInterface userInterface;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Optional<User> user = userInterface.getUser(username);
        if (user.isEmpty()) throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        return Mono.just(UserDetailsImplementation.build(user.get()));
    }
}
