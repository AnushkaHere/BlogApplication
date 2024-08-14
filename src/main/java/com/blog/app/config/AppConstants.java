package com.blog.app.config;

public class AppConstants {

    public static final Integer ADMIN=101;
    public static final Integer USER=102;

    public static final String[] PUBLIC_URLS = {
            "/api/public/**",
            "/api/auth/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "webjar/**"
    };
}
