package com.client.userService.controllers;

import com.client.userService.errors.messages.Errors;
import com.client.userService.models.DTO.users.User;
import com.client.userService.models.response.ResponseHandler;
import com.client.userService.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userService/userdata/")
public class BalanceController {
    @Autowired
    private UserService userService;
    @Autowired
    private Errors errors;

    @GetMapping("/getBalance/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> getBalance(@PathVariable String username) {
        Optional<User> optionalUser = userService.getUser(username);
        if(optionalUser.isEmpty()) return ResponseHandler.generateResponse(errors.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, optionalUser.get().getBalance());
    }

    @GetMapping("/getCode/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> getCode(Authentication authentication) {
        return userService.getCode(authentication.getName());
    }

    @PostMapping("/set2FA/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> set2FA(Authentication authentication) {
        return userService.set2FA(authentication.getName());
    }
}
