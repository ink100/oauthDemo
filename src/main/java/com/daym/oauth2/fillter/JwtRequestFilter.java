package com.daym.oauth2.filter;

import com.daym.oauth2.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String DEVICE_ID_COOKIE = "Device-Id";
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final List<String> whiteUrlList;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil, List<String> whiteUrlList) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.whiteUrlList = whiteUrlList;  // 接收动态白名单
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(jwtUtil.getAccessTokenName());

        String jwt = null;
        String username = null;
        String path = request.getRequestURI();
        String deviceId = request.getHeader("deviceId");  // 尝试从请求头中获取 Device-Id
        String remerberme=null;

        // 如果请求路径在白名单中，直接放行
        if (whiteUrlList.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 从授权头获取 token，并解析用户名
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            if (deviceId == null) {
                deviceId =jwtUtil.extractDeviceId(jwt);// 如果 Device-Id 不存在，生成或获取
            }
        }


        // 验证 token 和 deviceId，并在安全上下文中设置用户身份
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 使用 JwtUtil 验证 token 的有效性和 deviceId
            if (jwtUtil.validateToken(jwt, userDetails.getUsername(), deviceId)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 继续过滤链
        chain.doFilter(request, response);
    }


}
