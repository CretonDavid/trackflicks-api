package com.studio.trackflicks.service;

import com.studio.trackflicks.model.Role;
import com.studio.trackflicks.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    public void init() {
        String secretKey = "c9b90616b3532f6e9d8de50938f94592cfb0a18b7c52af5cdafc9bb758945b50";
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        long jwtExpiration = 1000 * 60 * 60; // 1 hour
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
        long refreshExpiration = 1000 * 60 * 60 * 24; // 1 day
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", refreshExpiration);
        userDetails = new User(1L, "John Doe", "John.doe123@gmail.com", "azerty", Role.USER, false, false);
    }

    @Test
    void testExtractCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("John.doe123@gmail.com", username);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertEquals("John.doe123@gmail.com", jwtService.extractUsername(token));
    }

    @Test
    void testGenerateTokenWithEmptySecretKey() {
        ReflectionTestUtils.setField(jwtService, "secretKey", " ");
        assertThrows(WeakKeyException.class,() -> jwtService.generateToken(userDetails));
    }

    @Test
    void testGenerateTokenWithInvalidExpirationTime() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1);
        String token = jwtService.generateToken(userDetails);
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testTokenValidation() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testInvalidTokenValidation() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        assertNotNull(refreshToken);
        assertEquals("John.doe123@gmail.com", jwtService.extractUsername(refreshToken));
    }

    @Test
    void testRefreshTokenValidation() {
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        assertTrue(jwtService.isTokenValid(refreshToken, userDetails));
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);
        String token = jwtService.generateToken(userDetails);
        Thread.sleep(2);
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }

}
