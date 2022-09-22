package com.client.userService.controllers;

import com.client.userService.models.dbModels.users.User;
import com.client.userService.services.user.UserService;
import com.client.userService.utilities.DTO.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userService")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUser/{name}")
    public Optional<User> getUser(@PathVariable String name) {
        return userService.getUser(name);
    }
    @PostMapping("/saveUser")
    public Boolean saveUser(@RequestBody User requestUser) { return userService.saveUser(requestUser); }
    @PostMapping("/register")
    public Boolean registerUser(@RequestBody User requestUser) { return userService.registerUser(requestUser); }
    @GetMapping("/isExists/{name}")
    public Boolean isExists(@PathVariable String name) { return userService.userExists(name); }
    @GetMapping("/isExistsByEmail/{email}")
    public Boolean isExistsByEmail(@PathVariable String email) { return userService.userExistsByEmail(email); }
    @GetMapping("/isExistsByAll/{name}/{email}")
    public Boolean isExistsByEmail(@PathVariable String name, @PathVariable String email) { return userService.userExistsByAll(name, email); }
    @GetMapping("/changePasswordIfExists/{name}/{password}/{code}")
    public Boolean changePasswordIfExists(@PathVariable String name, @PathVariable String password , @PathVariable String code) throws GeneralSecurityException { return userService.changePasswordIfExists(name, password, code); }
    @PostMapping("/updateUser")
    public Boolean test(@RequestBody User user) { return userService.updateUser(user); }
}
