package com.client.authorizationService.controllers;

import com.client.authorizationService.models.DTO.authorization.AuthorizationDTO;
import com.client.authorizationService.models.DTO.authorization.JWTResponseDTO;
import com.client.authorizationService.services.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/authorizationService")
public class AuthenticationController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/user/authorizeUser")
    public JWTResponseDTO authorizeUser(@RequestBody AuthorizationDTO authorizationDTO) {
        return authorizationService.authorizeUser(authorizationDTO);
    }

    @PostMapping("/user/registerUser")
    public JWTResponseDTO registerUser(@RequestBody AuthorizationDTO authorizationDTO) {
        return  authorizationService.registerUser(authorizationDTO);
    }

    @PostMapping("/user/resetPassword")
    public Boolean resetPassword(@RequestBody AuthorizationDTO authorizationDTO) {
        return authorizationService.resetPassword(authorizationDTO);
    }

    @PostMapping("/user/validateToken")
    public Boolean validateToken(@RequestBody AuthorizationDTO authorizationDTO, HttpServletRequest httpServletRequest) {
        return authorizationService.validateJWT(authorizationDTO, httpServletRequest);
    }
}
