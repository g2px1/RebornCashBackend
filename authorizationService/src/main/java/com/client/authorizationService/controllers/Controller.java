package com.client.authorizationService.controllers;

//import com.client.authorizationService.TestUserInterface;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.client.authorizationService.users.User;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/authorizationService")
public class Controller {
    @Autowired
    private UserInterface userInterface;

    @GetMapping("/user/{name}")
    public Mono<User> getUser(@PathVariable String name) {
        Optional<User> optionalUser = userInterface.getUser(name);
        if (optionalUser.isEmpty())
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found."));
        return Mono.just(optionalUser.get());
    }
}
