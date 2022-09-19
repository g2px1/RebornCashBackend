//package com.client.eurekaclient.services.authorization.filters;
//
//import com.client.eurekaclient.messages.ErrorMessage;
//import com.client.eurekaclient.services.requests.CachedBodyHttpServletRequest;
//import com.client.eurekaclient.utilities.VerificationUtility;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public class CaptchaFilter implements Filter {
//    private VerificationUtility verificationUtility;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ObjectMapper mapper = new ObjectMapper();
//        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
//        Object str = mapper.readValue(cachedBodyHttpServletRequest.getInputStream(), Object.class);
//        JSONObject jsonObject = new JSONObject(str);
//        if (!verificationUtility.isCaptchaValid(jsonObject.getString("captchaToken"), cachedBodyHttpServletRequest))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.BOT);
//        filterChain.doFilter(cachedBodyHttpServletRequest, servletResponse);
//    }
//}
