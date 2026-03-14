package com.qrplatform.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminUserSummaryDto {
    private UUID id;
    private String email;
    private String role;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public AdminUserSummaryDto(UUID id, String email, String role) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
	}
	public AdminUserSummaryDto() {}
}