package com.jithin.nudge.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.jithin.nudge.service.JWTService;
import com.jithin.nudge.util.JsonObjectMapper;
import com.jithin.nudge.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;

    public UserAuthenticationSuccessHandler(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        JsonObjectMapper objectMapper = new JsonObjectMapper();
        String jwtToken;
        String respString;
        try {
            jwtToken = jwtService.generateToken(authentication);
            respString = objectMapper.setJsonString(ResponseWrapper.ok("User Authenticated Successfully", jwtToken).getBody());
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            respString = objectMapper.setJsonString(ResponseWrapper.internalServerError("Error in generating token"));
            response.setStatus(400);
        }

        response.getWriter().write(respString);
    }

}
