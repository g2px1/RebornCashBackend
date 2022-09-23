package com.client.userService.services.openfeign.users;

import com.client.userService.models.DTO.users.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "userService")
public interface UserInterface {
    @GetMapping("/userService/getUser/{name}")
    Optional<User> getUser(@PathVariable String name);
    @PostMapping("/userService/saveUser")
    Boolean saveUser(@RequestBody User requestUser);
    @PostMapping("/userService/register")
    Boolean registerUser(@RequestBody User requestUser);
    @GetMapping("/userService/isExists/{name}")
    Boolean isExists(@PathVariable String name);
    @GetMapping("/userService/isExistsByEmail/{email}")
    Boolean isExistsByEmail(@PathVariable String email);
    @GetMapping("/userService/isExistsByAll/{name}/{email}")
    Boolean isExistsByAll(@PathVariable String name, @PathVariable String email);
    @GetMapping("/userService/changePasswordIfExists/changePasswordIfExists/{name}/{password}/{code}")
    Boolean changePasswordIfExists(@PathVariable String name, @PathVariable String password, @PathVariable String code);
}

