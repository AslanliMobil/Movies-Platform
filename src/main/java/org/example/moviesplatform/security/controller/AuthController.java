package org.example.moviesplatform.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.security.model.LoginRequest;
import org.example.moviesplatform.security.model.RegisterRequest;
import org.example.moviesplatform.security.service.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Yeni istifadəçi yaratmaq")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Sistemə giriş və token almaq")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
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
}