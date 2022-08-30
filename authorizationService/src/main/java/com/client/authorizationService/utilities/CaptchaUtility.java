package com.client.authorizationService.utilities;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.models.DTO.users.User;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CaptchaUtility {
    @Value("${app.url.recaptcha}")
    private String recatpchaUrl;
    @Value("${app.secretKey.recaptcha}")
    private String recaptchaSecretKey;
    @Value("${app.scoresLevel.recaptcha}")
    private BigDecimal scoresLevel;

    public CaptchaUtility() {}

    public boolean isCaptchaValid(String captchaToken, HttpServletRequest request) {
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
            return (score.compareTo(scoresLevel) >= 0);
        return json.getBoolean("success");
    }
    protected boolean isAuthenticatorValid(User user, int code) throws GeneralSecurityException {
        return TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), code, 0);
    }
}
