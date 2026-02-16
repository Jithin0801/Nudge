package com.jithin.nudge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.jithin.nudge.entity.UserPersonal;
import com.jithin.nudge.entity.UserSecurity;
import com.jithin.nudge.repository.UserSecurityRepository;
import com.jithin.nudge.util.JKSUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

class JWTServiceTest {

    @Mock
    private JKSUtil jksUtil;

    @Mock
    private UserSecurityRepository userSecurityRepository;

    @InjectMocks
    private JWTService jwtService;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Generate a real KeyPair for testing
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        when(jksUtil.loadPrivateKey()).thenReturn(privateKey);
        when(jksUtil.getPublicKey()).thenReturn(publicKey);
    }

    @Test
    void testGenerateTokenContainsFullName() throws Exception {
        // Arrange
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String expectedFullName = "John Doe";

        UserDetails userDetails = User.withUsername(email).password("password").authorities("USER").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmailId(email);
        UserPersonal userPersonal = new UserPersonal();
        userPersonal.setFirstName(firstName);
        userPersonal.setLastName(lastName);
        userSecurity.setUserPersonal(userPersonal);

        when(userSecurityRepository.findUserByEmailId(email)).thenReturn(userSecurity);

        // Act
        String token = jwtService.generateToken(authentication);

        // Assert
        assertNotNull(token);

        // Parse token to verify claims
        Claims claims = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();

        assertEquals(expectedFullName, claims.get("fullName"));
        assertEquals(email, claims.getIssuer());
    }
}
