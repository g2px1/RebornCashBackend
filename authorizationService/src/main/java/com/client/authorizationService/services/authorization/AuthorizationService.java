package com.client.authorizationService.services.authorization;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.models.DTO.authorization.AuthorizationDTO;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
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
    private JWTUtility jwtUtility;
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

    protected boolean isCaptchaNotValid(String captchaToken, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s", recaptchaSecretKey, captchaToken);
        String strResponse = restTemplate.postForObject(url,
                new HashMap<>(),
                String.class,
                new HashMap<>(Map.of("secret", recatpchaUrl,
                        "response", captchaToken,
                        "remoteip", request.getRemoteAddr())));
        JSONObject json = new JSONObject(strResponse);
        BigDecimal score;
        try {
            score = new BigDecimal(json.get("score").toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.DEFAULT_ERROR);
        }
        if (json.getBoolean("success"))
            return (score.compareTo(scoresLevel) < 0);
        return !json.getBoolean("success");
    }
    protected boolean isAuthenticatorValid(User user, int code) throws GeneralSecurityException {
        return TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), code, 0);
    }

    public JWTResponseDTO authorizeUser(AuthorizationDTO authorizationDTO, HttpServletRequest httpServletRequest) {
//        if (isCaptchaNotValid(authorizationDTO.captchaToken, httpServletRequest))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.BOT);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationDTO.username, authorizationDTO.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtility.generateJwtToken(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        Optional<User> optionalUser;
        try {
            optionalUser = userInterface.getUser(authorizationDTO.username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.DEFAULT_ERROR);
        }
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND);
        User user = optionalUser.get();
        if (user.isTwoFA()) verifyInterface.setVerify(new VerifyDTO(user.getUsername(), false));
        else user.setNonce(Rnd.get(Integer.MAX_VALUE));
        userInterface.saveUser(user);
        return new JWTResponseDTO(jwt, user.getUsername(), user.isTwoFA(), (user.isTwoFA()) ? user.getNonce() : -1);
    }

    public JWTResponseDTO registerUser(AuthorizationDTO authorizationDTO, HttpServletRequest httpServletRequest) {
//        if (isCaptchaNotValid(authorizationDTO.captchaToken, httpServletRequest))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.BOT);
        if (userInterface.isExistsByAll(authorizationDTO.username, authorizationDTO.email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_EXISTS);
        User user = new User(authorizationDTO.username.trim(), authorizationDTO.email.trim(), encoder.encode(authorizationDTO.password.trim()), new ArrayList<>(List.of(new Role(ERole.ROLE_USER))), TimeBasedOneTimePasswordUtil.generateBase32Secret());
        userInterface.saveUser(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authorizationDTO.getUsername(), authorizationDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtility.generateJwtToken(authentication);
        return new JWTResponseDTO(null, user.getUsername(), user.isTwoFA(), -1);
    }

    public boolean resetPassword(AuthorizationDTO authorizationDTO, HttpServletRequest httpServletRequest) {
//        if (isCaptchaNotValid(authorizationDTO.captchaToken, httpServletRequest))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.BOT);
        if (!userInterface.changePasswordIfExists(authorizationDTO.username, encoder.encode(authorizationDTO.password), authorizationDTO.code))
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.DEFAULT_ERROR);
        return true;
    }

    public boolean validateJWT(AuthorizationDTO authorizationDTO, HttpServletRequest httpServletRequest) {
        String jwt = parseJwt(httpServletRequest);
        return jwtUtility.validateJwtToken(jwt);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
