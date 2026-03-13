package com.qrplatform.backend.service.impl;

import com.qrplatform.backend.dto.response.AdminUserSummaryDto;
import com.qrplatform.backend.entity.PlaformAdminLogin; // note typo
import com.qrplatform.backend.exception.AdminNotFoundException;
import com.qrplatform.backend.exception.InvalidTokenException;
import com.qrplatform.backend.repository.AdminRepository;
import com.qrplatform.backend.service.AdminService;
import com.qrplatform.backend.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Explicit constructor
    public AdminServiceImpl(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder,
                            EmailService emailService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public PlaformAdminLogin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with email: " + email));
    }

    @Override
    public PlaformAdminLogin saveAdmin(PlaformAdminLogin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public void initiatePasswordReset(String email) {
        PlaformAdminLogin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with email: " + email));

        String token = UUID.randomUUID().toString();
        admin.setResetToken(token);
        admin.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        adminRepository.save(admin);

        emailService.sendResetEmail(admin.getEmail(), token);
        log.info("Password reset token generated for admin: {}", email);
    }

    @Override
    public void completePasswordReset(String token, String newPassword) {
        PlaformAdminLogin admin = adminRepository.findByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

        if (admin.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token has expired");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setResetToken(null);
        admin.setResetTokenExpiry(null);
        adminRepository.save(admin);
        log.info("Password reset completed for admin: {}", admin.getEmail());
    }

    @Override
    public List<AdminUserSummaryDto> getAllUsers() {
        return adminRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserSummaryDto getUserById(UUID id) {
        PlaformAdminLogin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        return mapToDto(admin);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!adminRepository.existsById(id)) {
            throw new AdminNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    private AdminUserSummaryDto mapToDto(PlaformAdminLogin admin) {
        // Using constructor instead of builder
        return new AdminUserSummaryDto(admin.getId(), admin.getEmail(), admin.getRole());
    }
}