package com.qrplatform.backend.controller;

import com.qrplatform.backend.dto.request.AdminForgotPasswordRequest;
import com.qrplatform.backend.dto.request.AdminLoginRequest;
import com.qrplatform.backend.dto.request.AdminResetPasswordRequest;
import com.qrplatform.backend.dto.response.AdminLoginResponse;
import com.qrplatform.backend.exception.AdminNotFoundException;
import com.qrplatform.backend.exception.InvalidTokenException;
import com.qrplatform.backend.service.AdminService;
import com.qrplatform.backend.service.RateLimiterService;
import com.qrplatform.backend.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminService adminService;
    private final RateLimiterService rateLimiterService;

    // Explicit constructor
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          AdminService adminService,
                          RateLimiterService rateLimiterService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminService = adminService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request,
                                                    HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();

        if (!rateLimiterService.isAllowed("login:" + clientIp, 5, 60)) {
            throw new RuntimeException("Too many login attempts. Please try again later.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        log.info("User logged in: {}", request.getEmail());

        return ResponseEntity.ok(new AdminLoginResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody AdminForgotPasswordRequest request,
                                                  HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();

        if (!rateLimiterService.isAllowed("forgot:" + clientIp, 3, 3600)) {
            throw new RuntimeException("Too many password reset requests. Please try again later.");
        }

        try {
            adminService.initiatePasswordReset(request.getEmail());
            log.info("Password reset initiated for: {}", request.getEmail());
        } catch (AdminNotFoundException e) {
            log.warn("Password reset attempt for non-existent email: {}", request.getEmail());
        }

        return ResponseEntity.ok("If the email exists, a reset link has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody AdminResetPasswordRequest request) {
        try {
            adminService.completePasswordReset(request.getToken(), request.getNewPassword());
            log.info("Password reset completed for token: {}", request.getToken());
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Invalid or expired reset token.");
        }
    }
}