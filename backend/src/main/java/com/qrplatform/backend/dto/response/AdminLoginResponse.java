package com.qrplatform.backend.dto.response;



public class AdminLoginResponse {

    private String token;
    private AdminUserSummaryDto admin;

    public AdminLoginResponse(String token) {
        this.token = token;
    }

    

	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public AdminUserSummaryDto getAdmin() {
		return admin;
	}



	public void setAdmin(AdminUserSummaryDto admin) {
		this.admin = admin;
	}



	public AdminLoginResponse(String token, AdminUserSummaryDto admin) {
        this.token = token;
        this.admin = admin;
    }
}