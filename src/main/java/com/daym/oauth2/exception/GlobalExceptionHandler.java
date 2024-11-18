package com.daym.oauth2.exception;

import cn.hutool.core.util.IdUtil;
import com.daym.oauth2.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理认证异常
    @ExceptionHandler({AuthenticationException.class,AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        logger.error("认证异常：{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "认证失败：" + ex.getMessage()));
    }

    // 处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("权限不足异常：{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN.value(), "权限不足，无法访问该资源"));
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        logger.error("参数校验异常：{}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "参数不合法：" + errors));
    }
    // 处理其他认证相关的异常
    @ExceptionHandler({ JwtException.class, ExpiredJwtException.class })
    public ResponseEntity<Object> handleJwtException(Exception ex) {
        // 创建错误响应体
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", "Invalid or expired token");

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    // 处理其他未捕获的异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        String errorId = IdUtil.nanoId(32);
        logger.error("errorId={}服务器内部错误：", errorId,ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误,错误编码为:"+errorId));
    }




}
