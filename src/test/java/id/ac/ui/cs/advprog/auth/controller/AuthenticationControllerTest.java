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
import java.util.Date;
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
                .firstname("testFirstName")
                .username("testUsername")
                .password("testPassword")
                .targetKalori(2000)
                .tanggalLahir("1998-01-30")
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

        String expectedResponseContent = "{\"message\":\"User registered successfully.\",\"success\":true,\"token\":{\"token\":\"dummyToken\"}}";

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
        String expectedResponseContent = "{\"message\":\"User with the same username already exist\",\"httpStatus\":\"BAD_REQUEST\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("register"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent, false));
    }

    @Test
    void testUsernameNotFoundException() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(UsernameNotFoundException.class);

        String expectedResponseContent = "{\"message\":null,\"httpStatus\":\"UNAUTHORIZED\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent, false));
    }

    @Test
    void testCredentialError() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(JwtException.class);

        String expectedResponseContent = "{\"message\":null,\"httpStatus\":\"UNAUTHORIZED\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.handler().methodName("login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent, false));
    }

    @Test
    void testLoginUser() throws Exception {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        String expectedResponseContent = "{\"message\":\"Login success\",\"success\":true,\"token\":{\"token\":\"dummyToken\"}}";

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
    void testGetUser() throws Exception {
        setUpMockUser();

        String expectedResponseContent = "{\"username\":\"testUsername\",\"id\":1}";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/get-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("getUser"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent));
    }

    @Test
    void testGetAllUserData() throws Exception {
        setUpMockUser();

        String expectedResponseContent = "{\"username\":\"testUsername\",\"id\":1,\"targetKalori\":2000,\"tanggalLahir\":\"2021-05-24T00:00:00.000+00:00\",\"beratBadan\":null,\"tinggiBadan\":null}";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/get-all-userdata")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("getAllUserData"))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponseContent));
    }

    private void setUpMockUser() {
        User user = User.builder()
                .id(1)
                .firstname("testFirstName")
                .username("testUsername")
                .password("testPassword")
                .targetKalori(2000)
                .tanggalLahir(new Date(1621814400000L))
                .build();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
    }
}
