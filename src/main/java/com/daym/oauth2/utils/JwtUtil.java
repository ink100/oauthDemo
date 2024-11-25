package com.daym.oauth2.utils;

import com.daym.oauth2.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = generateKeyFromString(jwtProperties.getSecret());
    }

    /**
     * 生成 Tokens，支持 accessToken 和 refreshToken，考虑 rememberMe 和 deviceId
     */
    public Map<String, String> generateTokens(String username, String deviceId, boolean rememberMe) {
        Map<String, String> tokens = new HashMap<>();

        // 根据 rememberMe 参数设置过期时间
        long accessTokenExpiry = rememberMe ? jwtProperties.getRememberMeAccessTokenExpiration() : jwtProperties.getAccessTokenExpiration();
        long refreshTokenExpiry = jwtProperties.getRefreshTokenExpiration();  // refreshToken 应该较短

        // 生成 accessToken 和 refreshToken，deviceId 会存入 token 中
        String accessToken = createAccessToken(username, deviceId,rememberMe);
        String refreshToken = createRefreshToken(username, deviceId,rememberMe);

        // 通过配置表定义的格式生成 Redis Key
        String accessTokenKey = jwtProperties.getRedisAccessTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);
        String refreshTokenKey = jwtProperties.getRedisRefreshTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);

        tokens.put(jwtProperties.getAccessTokenName(), accessToken);
        tokens.put(jwtProperties.getRefreshTokenName(), refreshToken);

        // 存储到 Redis 并设置不同的过期时间
        redisTemplate.opsForValue().set(accessTokenKey, accessToken, accessTokenExpiry, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, refreshTokenExpiry, TimeUnit.MILLISECONDS);

        return tokens;
    }

    /**
     * 使用指定字符串生成 SecretKey
     */
    private SecretKey generateKeyFromString(String keyString) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha256.digest(keyString.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("生成密钥失败", e);
        }
    }



    /**
     * 创建 AccessToken，通常有效期较长，带有较多的 claims 信息
     */
    private String createAccessToken(String username, String deviceId,boolean rememberMe) {
        return Jwts.builder()
                .setSubject(username)
                .claim("deviceId", deviceId)  // 将 deviceId 存入 token 中
                .claim("role", "user") // 例如可以添加角色等信息，增加长度
                .claim("permissions", "read,write")  // 可以添加权限信息，增加长度
                .claim("rememberMe", rememberMe)  //是否记住我
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(secretKey)  // 使用较强的签名算法来增加安全性
                .compact();
    }

    /**
     * 创建 RefreshToken，通常有效期较长，Token 内容简洁
     */
    private String createRefreshToken(String username, String deviceId,boolean rememberMe ) {
        return Jwts.builder()
                .setSubject(username)
                .claim("deviceId", deviceId)  // 将 deviceId 存入 token 中
                .claim("rememberMe", rememberMe)  //是否记住我
                .setIssuedAt(new Date(System.currentTimeMillis()))

                .signWith(secretKey)  // 使用较轻量的签名算法，保持 Token 较短
                .compact();
    }
    /**
     * 使用 refreshToken 刷新 accessToken
     */
    public String refreshAccessToken(String refreshToken, boolean rememberMe, String deviceId) {
        String username = extractUsername(refreshToken);

        // 获取 Redis 中的 refreshToken Key
        String refreshTokenKey = jwtProperties.getRedisRefreshTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);

        if (validateRefreshToken(refreshTokenKey)) {
            long newAccessTokenExpiry = rememberMe ? jwtProperties.getRememberMeAccessTokenExpiration() : jwtProperties.getAccessTokenExpiration();
            String newAccessToken = createAccessToken(username, deviceId,rememberMe);

            // 通过配置表定义的格式生成 accessToken Redis Key
            String accessTokenKey = jwtProperties.getRedisAccessTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);
            redisTemplate.opsForValue().set(accessTokenKey, newAccessToken, newAccessTokenExpiry, TimeUnit.MILLISECONDS);
            return newAccessToken;
        }
        throw new SecurityException("无效的 refreshToken，无法刷新 accessToken");
    }

    /**
     * 校验refreshToken
     * @param refreshTokenKey
     * @return
     */
    private boolean validateRefreshToken(String refreshTokenKey) {
        return redisTemplate.hasKey(refreshTokenKey);
    }

    /**
     * 校验 Token 是否有效（基于 Redis 过期时间）
     */
    public boolean validateToken(String jwt, String username, String deviceId) {
        boolean rememberMe=extractRemerberMe(jwt);
        String tokenKey=jwtProperties.getRedisAccessTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);
        // 直接检查 Redis 中是否存在该 token
        Boolean hasKey = redisTemplate.hasKey(tokenKey);
        if(hasKey){
            long newAccessTokenExpiry = rememberMe ? jwtProperties.getRememberMeAccessTokenExpiration() : jwtProperties.getAccessTokenExpiration();
            redisTemplate.expire(tokenKey, newAccessTokenExpiry, TimeUnit.MILLISECONDS);
        }
        return hasKey;
    }

    private boolean extractRemerberMe(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("deviceId", Boolean.class);  // 提取 deviceId
    }

    /**
     * 注销 Tokens：从 Redis 中移除 accessToken 和 refreshToken，基于 deviceId
     */
    public void invalidateTokens(String username, String deviceId) {
        // 通过配置表定义的格式生成 Redis Key
        String accessTokenKey = jwtProperties.getRedisAccessTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);
        String refreshTokenKey = jwtProperties.getRedisRefreshTokenKeyName().replace("{username}", username).replace("{deviceId}", deviceId);

        redisTemplate.delete(accessTokenKey);
        redisTemplate.delete(refreshTokenKey);
    }

    /**
     * 提取 Token 中的用户名
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 提取 Token 中的 deviceId
     */
    public String extractDeviceId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("deviceId", String.class);  // 提取 deviceId
    }

    public String getAccessTokenName() {
        return jwtProperties.getAccessTokenName();
    }

    public String getRefreshTokenName() {
        return jwtProperties.getRefreshTokenName();
    }
}
