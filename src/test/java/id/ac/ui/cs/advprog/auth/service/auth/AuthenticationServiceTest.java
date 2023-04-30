package id.ac.ui.cs.advprog.auth.service.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.repository.UserRepository;

import id.ac.ui.cs.advprog.auth.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;

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
}
