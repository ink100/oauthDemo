package com.daym.oauth2.config;

import com.daym.oauth2.entity.SysConfig;
import com.daym.oauth2.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String defaultSecretKey;

    @Value("${jwt.accessTokenExpiration}")
    private long defaultAccessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long defaultRefreshTokenExpiration;

    @Value("${jwt.redisAccessTokenKeyName}")
    private String defaultRedisAccessTokenKeyName;

    @Value("${jwt.redisRefreshTokenKeyName}")
    private String defaultRedisRefreshTokenKeyName;

    @Value("${jwt.accessTokenName}")
    private String defaultAccessTokenName;

    @Value("${jwt.refreshTokenName}")
    private String defaultRefreshTokenName;

    @Value("${jwt.enableRefreshToken}")
    private boolean defaultEnableRefreshToken;

    @Value("${jwt.rememberMeAccessTokenExpiration}")
    private long defaultRememberMeAccessTokenExpiration;

    @Value("${jwt.apiTokenName}")
    private String defaultApiTokenName;



    private final SysConfigService sysConfigService;

    @Autowired
    public JwtConfig(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @Bean
    public JwtProperties jwtProperties() {
        String secretKey = Optional.ofNullable(getConfigValue("jwt.secret")).orElse(defaultSecretKey);
        long accessTokenExpiration = Long.parseLong(Optional.ofNullable(getConfigValue("jwt.accessTokenExpiration")).orElse(String.valueOf(defaultAccessTokenExpiration)));
        long refreshTokenExpiration = Long.parseLong(Optional.ofNullable(getConfigValue("jwt.refreshTokenExpiration")).orElse(String.valueOf(defaultRefreshTokenExpiration)));
        String redisAccessTokenKeyName = Optional.ofNullable(getConfigValue("jwt.redisAccessTokenKeyName")).orElse(defaultRedisAccessTokenKeyName);
        String redisRefreshTokenKeyName = Optional.ofNullable(getConfigValue("jwt.redisRefreshTokenKeyName")).orElse(defaultRedisRefreshTokenKeyName);
        String accessTokenName = Optional.ofNullable(getConfigValue("jwt.accessTokenName")).orElse(defaultAccessTokenName);
        String refreshTokenName = Optional.ofNullable(getConfigValue("jwt.refreshTokenName")).orElse(defaultRefreshTokenName);
        boolean enableRefreshToken = Boolean.parseBoolean(Optional.ofNullable(getConfigValue("jwt.enableRefreshToken")).orElse(String.valueOf(defaultEnableRefreshToken)));
        long rememberMeAccessTokenExpiration = Long.parseLong(Optional.ofNullable(getConfigValue("jwt.rememberMeAccessTokenExpiration")).orElse(String.valueOf(defaultRememberMeAccessTokenExpiration)));
        String apiTokenName = Optional.ofNullable(getConfigValue("jwt.apiTokenName")).orElse(String.valueOf(defaultApiTokenName));

        return new JwtProperties(secretKey, accessTokenExpiration, refreshTokenExpiration,
                redisAccessTokenKeyName, redisRefreshTokenKeyName, accessTokenName, refreshTokenName,
                enableRefreshToken, rememberMeAccessTokenExpiration, apiTokenName);
    }

    /**
     * 从数据库中获取配置值
     */
    private String getConfigValue(String key) {
        SysConfig configEntity = sysConfigService.selectByConfigKey(key);
        return (configEntity != null) ? configEntity.getConfigValue() : null;
    }
}
