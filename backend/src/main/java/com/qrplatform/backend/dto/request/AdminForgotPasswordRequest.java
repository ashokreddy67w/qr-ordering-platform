package com.qrplatform.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AdminForgotPasswordRequest(
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
		super();
		this.email = email;
	}
	public AdminForgotPasswordRequest() {}
}