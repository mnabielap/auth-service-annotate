package id.ac.ui.cs.advprog.auth.service.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.auth.model.auth.Token;
import id.ac.ui.cs.advprog.auth.repository.TokenRepository;

import id.ac.ui.cs.advprog.auth.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    @Test
    void testLogoutValidToken() {
        String jwt = "validToken";
        String authHeader = "Bearer " + jwt;

        Token storedToken = Token.builder()
                .tokenData(jwt)
                .expired(false)
                .revoked(false)
                .build();

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenRepository.findByTokenData(jwt)).thenReturn(Optional.of(storedToken));

        logoutService.logout(request, response, authentication);

        assertTrue(storedToken.isExpired());
        assertTrue(storedToken.isRevoked());
        verify(tokenRepository, times(1)).save(storedToken);
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenRepository, times(1)).findByTokenData(jwt);
    }

    @Test
    void testLogoutInvalidToken() {
        String jwt = "invalidToken";
        String authHeader = "Bearer " + jwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenRepository.findByTokenData(jwt)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any(Token.class));
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenRepository, times(1)).findByTokenData(jwt);
    }

    @Test
    void testLogoutMissingAuthHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any(Token.class));
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenRepository, never()).findByTokenData(anyString());
    }

    @Test
    void testLogoutInvalidAuthHeader() {
        String authHeader = "InvalidHeader";

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any(Token.class));
        verify(request, times(1)).getHeader("Authorization");
        verify(tokenRepository, never()).findByTokenData(anyString());
    }
}
