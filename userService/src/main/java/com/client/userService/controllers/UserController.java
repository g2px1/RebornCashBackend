package com.client.userService.controllers;

import com.client.userService.models.request.UserSeekingRequest;
import com.client.userService.models.DTO.users.User;
import com.client.userService.models.response.UserResponse;
import com.client.userService.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Boolean updateUser(@RequestBody UserResponse userResponse) { return userService.updateUser(userResponse); }
    @PostMapping("/loadUsers")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> loadUsers(@RequestBody UserSeekingRequest userSeekingRequest) { return userService.loadUsersByPagination(userSeekingRequest); }
}
