package com.jithin.nudge.handler;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import com.jithin.nudge.util.JsonObjectMapper;
import com.jithin.nudge.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JsonObjectMapper objectMapper = new JsonObjectMapper();
        String respoString = objectMapper.setJsonString(ResponseWrapper.unauthorized(exception.getMessage()).getBody());
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respoString);
    }

}
