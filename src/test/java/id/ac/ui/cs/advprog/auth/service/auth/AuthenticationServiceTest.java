package id.ac.ui.cs.advprog.auth.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.auth.dto.TokenResponse;
import id.ac.ui.cs.advprog.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.auth.model.auth.Token;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationServiceTest {

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
    void setUp() {
        user = User.builder().username("testUsername").password("testPassword").build();
        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setUsername("johndoe");
        request.setPassword("password");
        request.setTargetKalori(2000);
        request.setTanggalLahir("1990-01-01");
        request.setBeratBadan(70);
        request.setTinggiBadan(175);

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .active(true)
                .username(request.getUsername())
                .password("encodedPassword")
                .role("USER")
                .targetKalori(request.getTargetKalori())
                .tanggalLahir(stringToDate(request.getTanggalLahir()))
                .beratBadan(request.getBeratBadan())
                .tinggiBadan(request.getTinggiBadan())
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        TokenResponse response = service.register(request);

        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        User user = User.builder()
                .username(request.getUsername())
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        TokenResponse response = service.authenticate(request);

        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testEqualUsername() {
        User userNow = userRepository.findByUsername("testUsername").get();
        assertEquals("testUsername", userNow.getUsername());
    }

    @Test
    void testWrongUsername() {
        Assertions.assertThrows(Exception.class, () -> {
            userRepository.findByUsername("wrongUsername").get();
        });
    }

    @Test
    void testUserAlreadyExistException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new User()));

        RegisterRequest registerRequest = RegisterRequest.builder().username("any").build();

        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            service.register(registerRequest);
        });
    }

    @Test
    void testUserNotFoundWhenLogin() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().username("any").build();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.authenticate(authenticationRequest);
        });
    }

    private static Date stringToDate(String s) {
        try {
            var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(s);
        } catch(Exception e) {
            return null;
        }
    }
}
