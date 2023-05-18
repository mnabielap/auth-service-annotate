package id.ac.ui.cs.advprog.auth.controller;


import id.ac.ui.cs.advprog.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.auth.dto.TokenResponse;
import id.ac.ui.cs.advprog.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.auth.dto.AuthResponseDto;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Form validation failed
            AuthResponseDto responseDTO = new AuthResponseDto();
            responseDTO.setMessage("Registration failed. Please check the provided data.");
            responseDTO.setSuccess(false);
            return ResponseEntity.badRequest().body(responseDTO);
        }

        TokenResponse registeredUser = authenticationService.register(registerRequest);

        AuthResponseDto responseDTO = new AuthResponseDto();
        responseDTO.setMessage("User registered successfully.");
        responseDTO.setSuccess(true);
        responseDTO.setToken(registeredUser);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login (
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            AuthResponseDto responseDTO = new AuthResponseDto();
            responseDTO.setMessage("Login failed. Please check the provided data.");
            responseDTO.setSuccess(false);
            return ResponseEntity.badRequest().body(responseDTO);
        }

        TokenResponse registeredUser = authenticationService.authenticate(request);

        AuthResponseDto responseDTO = new AuthResponseDto();
        responseDTO.setMessage("Login success");
        responseDTO.setSuccess(true);
        responseDTO.setToken(registeredUser);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/get-username")
    public ResponseEntity<String> getUsername() {
        try {
            var userLoggedIn = getCurrentUser();
            return ResponseEntity.ok(userLoggedIn.getUsername());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/get-userid")
    public ResponseEntity<Integer> getUserId() {
        try {
            var userLoggedIn = getCurrentUser();
            return ResponseEntity.ok(userLoggedIn.getId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    protected static User getCurrentUser() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
    }
}
