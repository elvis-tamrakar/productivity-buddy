package com.example.productivity_app.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtil Unit Tests")
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_SECRET = "testSecretKeyForJwtUtilTesting";
    private static final long TEST_EXPIRATION = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void shouldGenerateTokenSuccessfully() {
        // Act
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        // Act
        String token1 = jwtUtil.generateToken("user1@example.com", 1L);
        String token2 = jwtUtil.generateToken("user2@example.com", 2L);

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should verify valid token successfully")
    void shouldVerifyValidTokenSuccessfully() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

        // Assert
        assertNotNull(decodedJWT);
        assertEquals(TEST_EMAIL, decodedJWT.getSubject());
        assertEquals(TEST_USER_ID, decodedJWT.getClaim("userId").asLong());
    }

    @Test
    @DisplayName("Should throw exception when verifying invalid token")
    void shouldThrowExceptionWhenVerifyingInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.verifyToken(invalidToken));
    }

    @Test
    @DisplayName("Should throw exception when verifying malformed token")
    void shouldThrowExceptionWhenVerifyingMalformedToken() {
        // Arrange
        String malformedToken = "not.a.jwt.token";

        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.verifyToken(malformedToken));
    }

    @Test
    @DisplayName("Should get email from valid token successfully")
    void shouldGetEmailFromValidTokenSuccessfully() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        String email = jwtUtil.getEmailFromToken(token);

        // Assert
        assertEquals(TEST_EMAIL, email);
    }

    @Test
    @DisplayName("Should throw exception when getting email from invalid token")
    void shouldThrowExceptionWhenGettingEmailFromInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getEmailFromToken(invalidToken));
    }

    @Test
    @DisplayName("Should get user ID from valid token successfully")
    void shouldGetUserIdFromValidTokenSuccessfully() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        Long userId = jwtUtil.getUserIdFromToken(token);

        // Assert
        assertEquals(TEST_USER_ID, userId);
    }

    @Test
    @DisplayName("Should throw exception when getting user ID from invalid token")
    void shouldThrowExceptionWhenGettingUserIdFromInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getUserIdFromToken(invalidToken));
    }

    @Test
    @DisplayName("Should return false for non-expired token")
    void shouldReturnFalseForNonExpiredToken() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should return true for expired token")
    void shouldReturnTrueForExpiredToken() {
        // Arrange
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L); // 1 millisecond expiration
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        
        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    @DisplayName("Should return true for invalid token when checking expiration")
    void shouldReturnTrueForInvalidTokenWhenCheckingExpiration() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    @DisplayName("Should handle null token gracefully")
    void shouldHandleNullTokenGracefully() {
        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.verifyToken(null));
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getEmailFromToken(null));
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getUserIdFromToken(null));
        assertTrue(jwtUtil.isTokenExpired(null));
    }

    @Test
    @DisplayName("Should handle empty token gracefully")
    void shouldHandleEmptyTokenGracefully() {
        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> jwtUtil.verifyToken(""));
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getEmailFromToken(""));
        assertThrows(JWTVerificationException.class, () -> jwtUtil.getUserIdFromToken(""));
        assertTrue(jwtUtil.isTokenExpired(""));
    }

    @Test
    @DisplayName("Should generate token with correct expiration time")
    void shouldGenerateTokenWithCorrectExpirationTime() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        Date issuedAt = decodedJWT.getIssuedAt();
        Date expiresAt = decodedJWT.getExpiresAt();

        // Assert
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);
        
        // Check that issuedAt is recent (within last 5 seconds)
        long currentTime = System.currentTimeMillis();
        assertTrue(issuedAt.getTime() <= currentTime);
        assertTrue(issuedAt.getTime() >= currentTime - 5000); // Within last 5 seconds
        
        // Check that expiration is approximately TEST_EXPIRATION milliseconds after issuedAt
        long expectedExpiration = issuedAt.getTime() + TEST_EXPIRATION;
        long tolerance = 1000; // 1 second tolerance
        assertTrue(expiresAt.getTime() >= expectedExpiration - tolerance);
        assertTrue(expiresAt.getTime() <= expectedExpiration + tolerance);
    }

    @Test
    @DisplayName("Should generate token with correct claims")
    void shouldGenerateTokenWithCorrectClaims() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        // Act
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

        // Assert
        assertEquals(TEST_EMAIL, decodedJWT.getSubject());
        assertEquals(TEST_USER_ID, decodedJWT.getClaim("userId").asLong());
        assertNotNull(decodedJWT.getIssuedAt());
        assertNotNull(decodedJWT.getExpiresAt());
    }
}
