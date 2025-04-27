package com.nursetrack.web.controller.shared;


import com.nursetrack.service.AuthService;
import com.nursetrack.web.dto.request.auth.LoginRequest;
import com.nursetrack.web.dto.response.CurrentUserResponse;
import com.nursetrack.web.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(Authentication authentication)
    {
        return ResponseEntity.ok(authService.getCurrentUser(authentication.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication)
    {
        authService.logout(authentication.getName());

        return ResponseEntity.noContent().build();
    }
}