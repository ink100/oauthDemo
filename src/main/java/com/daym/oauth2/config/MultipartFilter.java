package com.daym.oauth2.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/12
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class MultipartFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    if (request instanceof HttpServletRequest httpRequest) {
      // 判断是否为 multipart 请求
      if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/")) {
        // 包装为 MultipartHttpServletRequest
        MultipartHttpServletRequest multipartRequest = new DefaultMultipartHttpServletRequest(httpRequest);
        chain.doFilter(multipartRequest, response);
        return;
      }
    }

    // 非 multipart 请求直接通过
    chain.doFilter(request, response);
  }
}
