package com.daym.oauth2.security.handler;

import com.daym.oauth2.response.ApiResponse;
import com.daym.oauth2.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // 单例
    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        String deviceId = Optional.ofNullable(request.getHeader("Device-Id")).orElse("nondevice"); // 获取 deviceId
        boolean rememberMe = request.getParameter("rememberMe") != null; // 获取 rememberMe

        // 生成 accessToken 和 refreshToken
        Map<String, String> tokens = jwtUtil.generateTokens(username, deviceId, rememberMe);

        // 将生成的 Tokens 放到响应头中
        response.setHeader(jwtUtil.getAccessTokenName(), "Bearer " + tokens.get(jwtUtil.getAccessTokenName()));
        response.setHeader(jwtUtil.getRefreshTokenName(), tokens.get(jwtUtil.getRefreshTokenName()));
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(ApiResponse.success(authentication)));
        // 可选：如果需要重定向到某个 URL，可以在此处设置
     /*   String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/home")
                .build()
                .toUriString();
        response.sendRedirect(redirectUrl);  // 登录成功后重定向到首页*/
    }


}
