package com.daym.oauth2.config;

import lombok.Data;
@Data
public class JwtProperties {
    private String secret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private String redisAccessTokenKeyName;
    private String redisRefreshTokenKeyName;
    private String accessTokenName;
    private String refreshTokenName;
    private boolean enableRefreshToken;
    private long rememberMeAccessTokenExpiration;
    private String apiTokenName;

    // 提供全参数构造函数用于注入默认配置
    public JwtProperties(String secret, long accessTokenExpiration, long refreshTokenExpiration,
                         String redisAccessTokenKeyName, String redisRefreshTokenKeyName,
                         String accessTokenName, String refreshTokenName, boolean enableRefreshToken,
                         long rememberMeAccessTokenExpiration,
                         String apiTokenName) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisAccessTokenKeyName = redisAccessTokenKeyName;
        this.redisRefreshTokenKeyName = redisRefreshTokenKeyName;
        this.accessTokenName = accessTokenName;
        this.refreshTokenName = refreshTokenName;
        this.enableRefreshToken = enableRefreshToken;
        this.rememberMeAccessTokenExpiration = rememberMeAccessTokenExpiration;
        this.apiTokenName = apiTokenName;
    }
}
