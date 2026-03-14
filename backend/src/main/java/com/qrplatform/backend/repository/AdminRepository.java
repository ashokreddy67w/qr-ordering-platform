package com.qrplatform.backend.repository;

import com.qrplatform.backend.entity.PlaformAdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<PlaformAdminLogin, UUID> {
    Optional<PlaformAdminLogin> findByEmail(String email);
    Optional<PlaformAdminLogin> findByResetToken(String resetToken);
}