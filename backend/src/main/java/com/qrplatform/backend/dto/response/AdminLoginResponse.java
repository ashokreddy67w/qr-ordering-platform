package com.qrplatform.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AdminLoginResponse(String token) {
		super();
		this.token = token;
	}
	public AdminLoginResponse() {}
}