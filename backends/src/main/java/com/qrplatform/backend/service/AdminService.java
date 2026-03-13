package com.qrplatform.backend.service;

import com.qrplatform.backend.dto.response.AdminUserSummaryDto;
import com.qrplatform.backend.entity.PlaformAdminLogin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
	PlaformAdminLogin findByEmail(String email);
	PlaformAdminLogin saveAdmin(PlaformAdminLogin admin);
    void initiatePasswordReset(String email);
    void completePasswordReset(String token, String newPassword);
    List<AdminUserSummaryDto> getAllUsers();
    AdminUserSummaryDto getUserById(UUID id);
    void deleteUser(UUID id);
}