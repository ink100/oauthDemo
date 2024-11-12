package com.daym.oauth2.security.handler;

import com.daym.oauth2.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  private final JwtUtil jwtUtil;

  public CustomLogoutSuccessHandler(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
    String username = authentication.getName();
    String deviceId = request.getHeader("Device-Id");

    // 从 Redis 中删除相关的 accessToken 和 refreshToken
    jwtUtil.invalidateTokens(username, deviceId);

    // 可选：重定向到登录页面
    response.sendRedirect("/login");  // 或者您希望的其他页面
  }
}
