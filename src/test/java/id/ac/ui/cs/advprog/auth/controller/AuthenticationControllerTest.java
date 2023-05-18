package id.ac.ui.cs.advprog.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.auth.dto.TokenResponse;
import id.ac.ui.cs.advprog.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.auth.service.AuthenticationService;
import id.ac.ui.cs.advprog.auth.service.JwtService;
import id.ac.ui.cs.advprog.auth.service.LogoutService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtService jwtService;
    @MockBean
    TokenRepository tokenRepository;
    @MockBean
    PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;
    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private TokenResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        registerRequest = RegisterRequest.builder()
                .username("testUsername")
                .password("testPassword")
                .build();
        authenticationRequest = AuthenticationRequest.builder()
                .username("testUsername")
                .password("testPassword")
                .build();
        authenticationResponse = TokenResponse.builder()
                .token("dummyToken")
                .build();
    }

    @Test
    void testRegisterUser() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        String expectedResponseContent = "{\"token\":\"dummyToken\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("register"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent));

        verify(authenticationService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void testUserAlreadyExistException() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenThrow(UserAlreadyExistException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("register"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testUsernameNotFoundException() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testCredentialError() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(JwtException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testLoginUser() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        String expectedResponseContent = "{\"token\":\"dummyToken\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("login"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent));

        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    void testLogoutUser() {
        LogoutHandler securityContextLogoutHandler = mock(SecurityContextLogoutHandler.class);
        LogoutHandler csrfLogoutHandler = mock(SecurityContextLogoutHandler.class);
        LogoutHandler logoutHandler = mock(LogoutService.class);
        List<LogoutHandler> logoutHandlers = Arrays.asList(securityContextLogoutHandler, csrfLogoutHandler, logoutHandler);
        LogoutHandler handler = new CompositeLogoutHandler(logoutHandlers);
        handler.logout(mock(HttpServletRequest.class), mock(HttpServletResponse.class), mock(Authentication.class));
        verify(securityContextLogoutHandler, times(1)).logout(any(HttpServletRequest.class),
                any(HttpServletResponse.class), any(Authentication.class));
        verify(csrfLogoutHandler, times(1)).logout(any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(Authentication.class));
        verify(logoutHandler, times(1)).logout(any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(Authentication.class));
    }

    @Test
    void testGetUsername() throws Exception {
        setUpMockUser();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/get-username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("getUsername"))
                .andExpect(MockMvcResultMatchers.content().string("testUsername"));
    }

    @Test
    void testGetUserId() throws Exception {
        setUpMockUser();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/get-userid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("getUserId"))
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

    private void setUpMockUser() {
        User user = User.builder().username("testUsername").id(1).build();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
    }
}
