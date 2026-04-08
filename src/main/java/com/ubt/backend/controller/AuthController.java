package com.ubt.backend.controller;

import com.ubt.backend.dto.AdminLoginRequest;
import com.ubt.backend.dto.ApiResponse;
import com.ubt.backend.dto.AuthResponse;
import com.ubt.backend.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.admin.password}")
    private String adminPassword;

    /**
     * POST /api/auth/login
     * Body: { "password": "ubtech@admin2025" }
     * Returns a JWT token valid for 24 hours
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AdminLoginRequest request) {

        if (!adminPassword.equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid admin password"));
        }

        String token = jwtUtils.generateAdminToken();

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .expiresIn(jwtUtils.getExpirationMs())
                .message("Login successful")
                .build();

        return ResponseEntity.ok(ApiResponse.ok(authResponse, "Admin authenticated successfully"));
    }

    /**
     * GET /api/auth/verify
     * Verifies the current token is valid (requires Authorization: Bearer <token>)
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify() {
        // If this endpoint is reached, the JWT filter has already authenticated the request
        return ResponseEntity.ok(ApiResponse.ok("ADMIN", "Token is valid"));
    }
}
