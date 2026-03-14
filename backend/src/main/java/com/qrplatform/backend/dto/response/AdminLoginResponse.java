package com.qrplatform.backend.dto.response;



public class AdminLoginResponse {

    private String token;
    private AdminUserSummaryDto admin;

    public AdminLoginResponse(String token) {
        this.token = token;
    }

    

	public AdminLoginResponse(String token, AdminUserSummaryDto admin) {
        this.token = token;
        this.admin = admin;
    }
}