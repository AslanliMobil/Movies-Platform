package org.example.moviesplatform.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.security.model.LoginRequest;
import org.example.moviesplatform.security.model.PasswordResetConfirmRequest;
import org.example.moviesplatform.security.model.PasswordResetRequest;
import org.example.moviesplatform.security.model.PasswordResetVerifyRequest;
import org.example.moviesplatform.security.model.RegisterRequest;
import org.example.moviesplatform.security.service.AuthUserService;
import org.example.moviesplatform.security.service.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Qeydiyyat və Giriş əməliyyatları")
public class AuthController {

    private final AuthUserService userService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Operation(summary = "Cari istifadəçi məlumatı (JWT ilə)")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
    }

    @Operation(summary = "Yeni istifadəçi yaratmaq")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Sistemə giriş və token almaq")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 1. Username və Password yoxlanılır
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // 2. Əgər bura çatdıqsa, giriş uğurludur. Token yaradırıq.
            // Qeyd: Əgər JwtService klassın varsa, onu bura çağır.
            // Yoxdursa, userService daxilində token yaradan metod yazıb onu çağır.
            String token = userService.generateToken(authenticate);

            return ResponseEntity.ok(Map.of("token", "Bearer " + token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "İstifadəçi adı və ya şifrə yanlışdır!"));
        }
    }

    @Operation(summary = "Şifrə yeniləmə üçün OTP göndər (email-ə)")
    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.requestOtp(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "OTP kodu doğrula")
    @PostMapping("/password-reset/verify")
    public ResponseEntity<Void> verifyPasswordReset(@Valid @RequestBody PasswordResetVerifyRequest request) {
        passwordResetService.verifyOtp(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "OTP ilə şifrəni yenilə")
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.confirmReset(request);
        return ResponseEntity.noContent().build();
    }
}