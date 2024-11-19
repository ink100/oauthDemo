package com.daym.oauth2.security.handler;

import com.daym.oauth2.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // 单例化 ObjectMapper
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

    // 自定义登录失败逻辑，可以返回特定的错误信息
//    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // 构建 ApiResponse
    ApiResponse<String> apiResponse = ApiResponse.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "username or password is incorrect");

    // 写入响应
    writeResponse(response, apiResponse);

    // 记录日志
  }

  private void writeResponse(HttpServletResponse response, ApiResponse<String> apiResponse) throws IOException {
    response.getWriter().write(OBJECT_MAPPER.writeValueAsString(apiResponse));
  }

}
