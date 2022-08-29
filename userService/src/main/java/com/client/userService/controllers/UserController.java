package com.client.userService.controllers;

import com.client.userService.models.dbModels.users.User;
import com.client.userService.services.user.UserService;
import com.client.userService.utilities.DTO.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userService/")
public class UserController {
    @Autowired
    private UserService userService;
    private Mapper mapper;

    @GetMapping("/getUser/{name}")
    public Optional<User> getUser(@PathVariable String name) {
        return userService.getUser(name);
    }
    @PostMapping("/saveUser")
    public Boolean saveUser(@RequestBody User requestUser) {
        return userService.saveUser(requestUser);
    }
    @GetMapping("/isExists/{name}")
    public Boolean saveUser(@PathVariable String name) {
        return userService.userExists(name);
    }
}
