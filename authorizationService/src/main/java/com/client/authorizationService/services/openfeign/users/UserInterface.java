package com.client.authorizationService.services.openfeign.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.client.authorizationService.users.User;

import java.util.Optional;

@FeignClient(name = "userService")
public interface UserInterface {
    @GetMapping("/userService/getUser/{name}")
    Optional<User> getUser(@PathVariable String name);
}
