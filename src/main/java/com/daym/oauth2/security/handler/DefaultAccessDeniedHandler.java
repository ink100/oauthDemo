package com.daym.oauth2.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/11
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Component
public class DefaultAccessDeniedHandler  implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // 返回 403 Forbidden
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: You do not have permission to access this resource");
    }
}