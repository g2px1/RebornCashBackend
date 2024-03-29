package com.client.authorizationService.services.authorization.filters;

import com.client.authorizationService.errors.messages.ErrorMessage;
import com.client.authorizationService.errors.messages.Errors;
import com.client.authorizationService.services.requests.CachedBodyHttpServletRequest;
import com.client.authorizationService.utilities.CaptchaUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class CaptchaFilter implements Filter {
    @Autowired
    private Errors errors;
    private CaptchaUtility captchaUtility;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
        Object str = mapper.readValue(cachedBodyHttpServletRequest.getInputStream(), Object.class);
        JSONObject jsonObject = new JSONObject(str);
        if (!jsonObject.has("captchaToken") || !captchaUtility.isCaptchaValid(jsonObject.getString("captchaToken"), cachedBodyHttpServletRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.BOT);
        filterChain.doFilter(cachedBodyHttpServletRequest, servletResponse);
    }
}
