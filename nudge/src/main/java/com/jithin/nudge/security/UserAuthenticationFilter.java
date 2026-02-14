package com.jithin.nudge.security;

import java.io.IOException;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UserAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public @Nullable Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String credentials = (String) request.getAttribute("credentials");

        if (credentials == null || !credentials.contains(":")) {
            throw new AuthenticationCredentialsNotFoundException("Credentials not found or invalid format");
        }

        String[] parts = credentials.split(":", 2);
        String username = parts[0];
        String password = parts[1];

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
