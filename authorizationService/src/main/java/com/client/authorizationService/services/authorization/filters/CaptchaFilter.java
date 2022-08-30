//package com.client.authorizationService.services.authorization.filters;
//
//import com.client.authorizationService.utilities.CaptchaUtility;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.tomcat.util.json.JSONParser;
//import org.json.JSONObject;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.stream.Collectors;
//
//public class CaptchaFilter implements Filter {
//    private CaptchaUtility captchaUtility;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
////        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
////        ObjectMapper mapper = new ObjectMapper();
////        Object str = mapper.readValue(servletRequest.getInputStream(), Object.class);
//        JSONObject jObj = new JSONObject(getBody((HttpServletRequest) servletRequest));
//        System.out.println(jObj);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    public static String getBody(HttpServletRequest request)  {
//
//        String body = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//
//        try {
//            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//                bufferedReader.close();
//            } else {
//                stringBuilder.append("");
//            }
//        } catch (IOException ex) {
//            // throw ex;
//            return "";
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    System.out.println(ex.getMessage());
//                }
//            }
//        }
//
//        body = stringBuilder.toString();
//        return body;
//    }
//}
