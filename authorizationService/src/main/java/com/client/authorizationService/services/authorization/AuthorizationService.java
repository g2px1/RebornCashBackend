package com.client.authorizationService.services.authorization;

import com.client.authorizationService.errors.messages.Errors;
import com.client.authorizationService.models.DTO.authorization.AuthorizationDTO;
import com.client.authorizationService.models.DTO.response.ResponseHandler;
import com.client.authorizationService.models.JWT.JWS;
import com.client.authorizationService.models.DTO.authorization.JWTResponseDTO;
import com.client.authorizationService.models.DTO.users.ERole;
import com.client.authorizationService.models.DTO.users.Role;
import com.client.authorizationService.models.DTO.users.User;
import com.client.authorizationService.models.DTO.verify.VerifyDTO;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import com.client.authorizationService.services.openfeign.verify.VerifyInterface;
import com.client.authorizationService.utilities.JWT.JWTUtility;
import com.client.authorizationService.utilities.random.Rnd;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class AuthorizationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private VerifyInterface verifyInterface;
    @Value("${app.url.recaptcha}")
    private String recatpchaUrl;
    @Value("${app.secretKey.recaptcha}")
    private String recaptchaSecretKey;
    @Value("${app.scoresLevel.recaptcha}")
    private BigDecimal scoresLevel;
    @Autowired
    private volatile JWS jws;
    @Autowired
    private Errors errors;
    @Autowired
    private JWTUtility jwtUtility;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    public ResponseEntity<Object> authorizeUser(AuthorizationDTO authorizationDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationDTO.username, authorizationDTO.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jws.generateJWS(authentication.getName());
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        Optional<User> optionalUser;
        try {
            optionalUser = userInterface.getUser(authorizationDTO.username);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if (optionalUser.isEmpty())
            return ResponseHandler.generateResponse(errors.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        User user = optionalUser.get();
        if (user.isTwoFA()) verifyInterface.setVerify(new VerifyDTO(user.getUsername(), false));
        else user.setNonce(Rnd.get(Integer.MAX_VALUE));
        userInterface.saveUser(user);
        return ResponseHandler.generateResponse(null, HttpStatus.OK,  new JWTResponseDTO(jwt, user.getUsername(), user.isTwoFA(), (user.isTwoFA()) ? user.getNonce() : -1));
    }

    public ResponseEntity<Object> registerUser(AuthorizationDTO authorizationDTO) {
        if (userInterface.isExistsByAll(authorizationDTO.username, authorizationDTO.email))
            return ResponseHandler.generateResponse(errors.USER_EXISTS, HttpStatus.BAD_REQUEST, null);
        User user = new User(authorizationDTO.username.trim(), authorizationDTO.email.trim(), encoder.encode(authorizationDTO.password.trim()), new ArrayList<>(List.of(new Role(ERole.ROLE_USER))), TimeBasedOneTimePasswordUtil.generateBase32Secret());
        userInterface.registerUser(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authorizationDTO.getUsername(), authorizationDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jws.generateJWS(authentication.getName());
        return ResponseHandler.generateResponse(null, HttpStatus.OK,  new JWTResponseDTO(jwt, user.getUsername(), user.isTwoFA(), (user.isTwoFA()) ? user.getNonce() : -1));
    }

    public ResponseEntity<Object> resetPassword(AuthorizationDTO authorizationDTO) {
        if (!userInterface.changePasswordIfExists(authorizationDTO.username, encoder.encode(authorizationDTO.password), authorizationDTO.code))
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, true);
    }

    public ResponseEntity<Object> approveVerifyCode(String username, String code) {
        Optional<User> user = userInterface.getUser(username);
        if (user.isEmpty()) return ResponseHandler.generateResponse(errors.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.get().getSecretKey(), Integer.parseInt(code), 0)) return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.BAD_REQUEST, null);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        verifyInterface.deleteVerify(new VerifyDTO(username, true));
        return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
    }

    public ResponseEntity<Object> validateJWT(String authorizationHeader) {
        String jwt = parseJwt(authorizationHeader);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, jwtUtility.validateJwtToken(jwt));
    }

    private String parseJwt(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer "))
            return authorizationHeader.substring(7, authorizationHeader.length());
        return null;
    }

    public Map<String, Object> getECKeyData() {
        return jws.getEcKey().toJSONObject();
    }
}
