package id.ac.ui.cs.advprog.auth.controller;


import id.ac.ui.cs.advprog.auth.dto.*;
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
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Form validation failed
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder()
                        .message("Registration failed. Please check the provided data.")
                        .success(false)
                        .build());
        }

        TokenResponse registeredUser = authenticationService.register(registerRequest);

        return ResponseEntity.ok(AuthenticationResponse.builder()
                        .message("User registered successfully.")
                        .success(true)
                        .token(registeredUser)
                        .build());
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder()
                        .message("Login failed. Please check the provided data.")
                        .success(false)
                        .build());
        }

        TokenResponse registeredUser = authenticationService.authenticate(request);

        return ResponseEntity.ok(AuthenticationResponse.builder()
                        .message("Login success")
                        .success(true)
                        .token(registeredUser)
                        .build());
    }

    @GetMapping("/get-user")
    public ResponseEntity<GetUserResponse> getUser() {
        try {
            var userLoggedIn = getCurrentUser();
            return ResponseEntity.ok(GetUserResponse.builder()
                    .username(userLoggedIn.getUsername())
                    .id(userLoggedIn.getId())
                    .build());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/get-all-userdata")
    public ResponseEntity<GetAllUserDataResponse> getAllUserData() {
        try {
            var userLoggedIn = getCurrentUser();
            return ResponseEntity.ok(GetAllUserDataResponse.builder()
                    .username(userLoggedIn.getUsername())
                    .id(userLoggedIn.getId())
                    .targetKalori(userLoggedIn.getTargetKalori())
                    .tanggalLahir(userLoggedIn.getTanggalLahir())
                    .beratBadan(userLoggedIn.getBeratBadan())
                    .tinggiBadan(userLoggedIn.getTinggiBadan())
                    .build());
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
