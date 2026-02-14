package com.jithin.nudge.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationManager implements AuthenticationManager {

    private UserAuthenticationProvider userAuthenticationProvider;

    public UserAuthenticationManager(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (userAuthenticationProvider.supports(authentication.getClass())) {
            return userAuthenticationProvider.authenticate(authentication);
        }
        return null;
    }

}
