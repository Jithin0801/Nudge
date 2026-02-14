package com.jithin.nudge.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class HttpBasicDecodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String authBearer = httpServletRequest.getHeader("Authorization");

        if (authBearer.startsWith("Basic")) {
            String base64 = authBearer.split(" ")[1];
            byte[] decodedByte = Base64.getDecoder().decode(base64);
            String plainCreds = new String(decodedByte, StandardCharsets.UTF_8);
            request.setAttribute("credentials", plainCreds);
        }

        chain.doFilter(request, response);
    }
}
