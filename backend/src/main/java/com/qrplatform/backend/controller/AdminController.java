package com.qrplatform.backend.controller;

import com.qrplatform.backend.dto.response.AdminUserSummaryDto;
import com.qrplatform.backend.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    // Explicit constructor
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        log.info("Admin accessed dashboard");
        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserSummaryDto>> getAllUsers() {
        List<AdminUserSummaryDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserSummaryDto> getUserById(@PathVariable UUID id) {
        AdminUserSummaryDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        log.info("Admin deleted user with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}