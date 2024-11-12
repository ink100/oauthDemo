package com.daym.oauth2.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    // 自定义登录失败逻辑，可以返回特定的错误信息
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("Authentication failed: " + exception.getMessage());
  }
}
