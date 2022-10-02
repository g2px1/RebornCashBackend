package com.client.userService.controllers;

import com.client.userService.messages.ErrorMessage;
import com.client.userService.models.DTO.users.User;
import com.client.userService.models.response.ResponseHandler;
import com.client.userService.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userService/balance/")
public class BalanceController {
    @Autowired
    private UserService userService;

    @GetMapping("/getBalance/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> getBalance(@PathVariable String username) {
        Optional<User> optionalUser = userService.getUser(username);
        if(optionalUser.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, optionalUser.get().getBalance());
    }
}
