package com.daym.oauth2.security.handler;

import com.daym.oauth2.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义未授权访问处理器，支持多种 Content-Type 请求体解析
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // 单例化 ObjectMapper

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // 设置 401 状态码和 JSON 响应头
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 构建 ApiResponse
        ApiResponse<String> apiResponse = ApiResponse.of(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing or invalid",null);

        // 写入响应
        writeResponse(response, apiResponse);

        // 记录日志
        logUnauthorizedAccess(request, authException);
    }

    private void writeResponse(HttpServletResponse response, ApiResponse<String> apiResponse) throws IOException {
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(apiResponse));
    }

    private void logUnauthorizedAccess(HttpServletRequest request, AuthenticationException authException) {
        log.error("Unauthorized access detected:");
        log.error("Path: {}", request.getRequestURI());
        log.error("Method: {}", request.getMethod());
        log.error("Client IP: {}", request.getRemoteAddr());

        String contentType = request.getContentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);
        if (contentType != null) {
            log.error("Content-Type: {}", contentType);

            if (mediaType.includes(MediaType.MULTIPART_FORM_DATA)) {
                handleMultipartBody(request);
            } else if (mediaType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
                handleUrlEncodedBody(request);
            } else if (mediaType.includes(MediaType.APPLICATION_JSON)) {
                handleJsonBody(request);
            } else {
                log.error("Unsupported or unhandled Content-Type.");
            }
        }

        log.error("Error Message: {}", authException.getMessage(), authException);
    }
    private void handleMultipartBody(HttpServletRequest request) {
        /*if (!(request instanceof MultipartHttpServletRequest multipartRequest)) {
            log.error("MultipartHttpServletRequest not detected. Ensure a MultipartResolver is configured.");
            return;
        }*/

        try {
            Map<String, Object> jsonMap = new HashMap<>();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 处理普通表单字段
            multipartRequest.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 1) {
                    jsonMap.put(key, values); // 多值字段
                } else if (values != null && values.length == 1) {
                    jsonMap.put(key, values[0]); // 单值字段
                } else {
                    jsonMap.put(key, null); // 空字段
                }
            });

            // 处理文件字段
            Map<String, Object> fileMap = new HashMap<>();
            multipartRequest.getFileMap().forEach((fieldName, file) -> {
                if (!file.isEmpty()) {
                    Map<String, Object> fileDetails = new HashMap<>();
                    fileDetails.put("originalFilename", file.getOriginalFilename());
                    fileDetails.put("size", file.getSize());
                    fileDetails.put("contentType", file.getContentType());
                    fileMap.put(fieldName, fileDetails);
                } else {
                    fileMap.put(fieldName, "EMPTY_FILE");
                }
            });
            jsonMap.put("files", fileMap);

            // 输出为 JSON 字符串
            String jsonString = new ObjectMapper().writeValueAsString(jsonMap);
            log.info("Multipart Form Data as JSON: {}", jsonString);

        } catch (Exception e) {
            log.error("Failed to process multipart request: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理 multipart/form-data 类型的请求
     *
     * @param request 请求对象
     */
    private void handleUrlEncodedBody(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (parameterMap.isEmpty()) {
            log.warn("No URL-encoded parameters found in the request.");
            return;
        }

        try {
            Map<String, Object> jsonMap = new HashMap<>();
            parameterMap.forEach((key, values) -> {
                if (values != null && values.length > 1) {
                    jsonMap.put(key, values); // 多值字段
                } else if (values != null && values.length == 1) {
                    jsonMap.put(key, values[0]); // 单值字段
                } else {
                    jsonMap.put(key, null); // 空字段
                }
            });

            // 输出为 JSON 字符串
            String jsonString = new ObjectMapper().writeValueAsString(jsonMap);
            log.info("URL-encoded Form Data as JSON: {}", jsonString);

        } catch (Exception e) {
            log.error("Failed to process URL-encoded request: {}", e.getMessage(), e);
        }
    }
    /**
     * 处理 application/json 类型的请求
     *
     * @param request 请求对象
     */
    private void handleJsonBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            log.error("Failed to read JSON body: {}", e.getMessage(), e);
        }
        log.error("JSON Body: {}", stringBuilder.toString());
    }
}
