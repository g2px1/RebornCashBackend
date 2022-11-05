package com.client.authorizationService.controllers;

import com.client.authorizationService.models.DTO.authorization.AuthorizationDTO;
import com.client.authorizationService.services.authorization.AuthorizationService;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/authorizationService")
public class AuthenticationController {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserInterface userInterface;

    @PostMapping("/user/authorizeUser")
    public ResponseEntity<Object> authorizeUser(@RequestBody AuthorizationDTO authorizationDTO) {
        System.out.println(authorizationDTO.username);
        return authorizationService.authorizeUser(authorizationDTO);
    }

    @PostMapping("/user/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody AuthorizationDTO authorizationDTO) {
        return  authorizationService.registerUser(authorizationDTO);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody AuthorizationDTO authorizationDTO) {
        return authorizationService.resetPassword(authorizationDTO);
    }

    @PostMapping("/user/validateToken")
    public ResponseEntity<Object> validateToken(@RequestBody String authorizationHeader) {
        return authorizationService.validateJWT(authorizationHeader);
    }

    @PostMapping("/user/approveVerifyCode")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> approveVerifyCode(@RequestBody AuthorizationDTO authorizationDTO, Authentication authentication) {
        return authorizationService.approveVerifyCode(authentication.getName(), authorizationDTO.code);
    }
}
