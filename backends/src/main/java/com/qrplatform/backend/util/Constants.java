package com.qrplatform.backend.util;

public final class Constants {
    private Constants() {}

    // Roles
    public static final String ROLE_PLATFORM_ADMIN = "PLATFORM_ADMIN";
    public static final String ROLE_RESTAURANT_OWNER = "RESTAURANT_OWNER";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    // JWT
    public static final long JWT_EXPIRATION_MS = 3600000; // 1 hour

    // Password reset
    public static final int PASSWORD_RESET_TOKEN_EXPIRY_HOURS = 1;

    // Rate limiting
    public static final int LOGIN_MAX_ATTEMPTS = 5;
    public static final int LOGIN_WINDOW_SECONDS = 60;
    public static final int FORGOT_PASSWORD_MAX_ATTEMPTS = 3;
    public static final int FORGOT_PASSWORD_WINDOW_SECONDS = 3600; // 1 hour

    // Redis keys
    public static final String BLACKLIST_PREFIX = "blacklist:";
    public static final String RATE_LIMIT_PREFIX = "rate:";
}