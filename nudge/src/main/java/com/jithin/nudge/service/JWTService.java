package com.jithin.nudge.service;

import java.security.PrivateKey;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.jithin.nudge.util.JKSUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

@Service
public class JWTService {

    private JKSUtil jksUtil;
    private final com.jithin.nudge.repository.UserSecurityRepository userSecurityRepository;

    public static final long TOKEN_VALIDITY = 10 * 60 * 60;

    public JWTService(JKSUtil jksUtil, com.jithin.nudge.repository.UserSecurityRepository userSecurityRepository) {
        this.jksUtil = jksUtil;
        this.userSecurityRepository = userSecurityRepository;
    }

    public String extractUsername(String token) throws Exception {
        try {
            final Jwt<?, ?> obj = Jwts.parser().verifyWith(jksUtil.getPublicKey()).build().parse(token);
            Claims claims = (Claims) obj.getPayload();
            String userName = claims.getIssuer();
            return userName;
        } catch (Exception e) {
            throw e;
        }
    }

    public String generateToken(org.springframework.security.core.Authentication authentication) throws Exception {
        PrivateKey privateKey = jksUtil.loadPrivateKey();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        com.jithin.nudge.entity.UserSecurity user = userSecurityRepository.findUserByEmailId(username);
        String fullName = "";
        if (user != null && user.getUserPersonal() != null) {
            fullName = user.getUserPersonal().getFirstName() + " " + user.getUserPersonal().getLastName();
        }

        return Jwts.builder().issuer(username).claim("fullName", fullName).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)).signWith(privateKey).compact();
    }


    public boolean isTokenValid(String jwt, UserDetails userDetails) throws Exception {
        try {
            String username = userDetails.getUsername();
            final Jwt<?, ?> obj = Jwts.parser().verifyWith(jksUtil.getPublicKey()).build().parse(jwt);
            Claims claims = (Claims) obj.getPayload();
            Boolean isTokenExpired = claims.getExpiration().before(new Date());
            return (username.equals(userDetails.getUsername().toString()) && !isTokenExpired);
        } catch (Exception e) {
            throw e;
        }
    }
}
