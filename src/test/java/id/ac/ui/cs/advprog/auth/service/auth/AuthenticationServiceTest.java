package id.ac.ui.cs.advprog.auth.service.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.auth.repository.UserRepository;

import id.ac.ui.cs.advprog.auth.service.AuthenticationService;
import id.ac.ui.cs.advprog.auth.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder().username("testUsername").password("testPassword").build();
        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
    }

    @Test
    public void testEqualUsername() {
        User userNow = userRepository.findByUsername("testUsername").get();
        Assertions.assertEquals(userNow.getUsername(), "testUsername");
    }

    @Test
    public void testWrongUsername() {
        Assertions.assertThrows(Exception.class, () -> {
            userRepository.findByUsername("wrongUsername").get();
        });
    }

    @Test
    public void testUserAlreadyExistException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            service.register(RegisterRequest.builder().username("any").build());
        });
    }

    @Test
    public void testUserNotFoundWhenLogin() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.authenticate(AuthenticationRequest.builder().username("any").build());
        });
    }
}
