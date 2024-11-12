package com.daym.oauth2.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SwitchAccountSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
        // 执行切换账号后的逻辑，比如清除当前的 JWT token，要求用户重新登录
        response.setHeader("Authorization", "");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Account switched successfully. Please log in again.");
    }
}
