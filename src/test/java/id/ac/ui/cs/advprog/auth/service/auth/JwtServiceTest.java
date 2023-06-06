package id.ac.ui.cs.advprog.auth.service.auth;

import id.ac.ui.cs.advprog.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private String validToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("john.doe");
        validToken = jwtService.generateToken(userDetails);
    }

    @Test
    void extractUsername_ValidToken_ReturnsUsername() {
        String extractedUsername = jwtService.extractUsername(validToken);
        assertEquals("john.doe", extractedUsername);
    }

    @Test
    void generateToken_DefaultClaims_ReturnsToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void isTokenValid_ValidTokenAndMatchingUserDetails_ReturnsTrue() {
        boolean isTokenValid = jwtService.isTokenValid(validToken, userDetails);
        assertTrue(isTokenValid);
    }
}