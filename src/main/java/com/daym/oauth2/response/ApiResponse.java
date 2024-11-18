package com.daym.oauth2.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.List;

// ApiResponse 类
@NoArgsConstructor
@Data
public class ApiResponse<T> {
    private int code;             // 状态码：200 - 成功，非200 - 错误
    private String message;       // 响应信息
    private T data;               // 成功时的数据
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors; // 字段级错误（如果有）
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // 错误响应的时间戳

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    // 失败响应（无字段错误）
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    // 失败响应（带有字段级错误）
    public static <T> ApiResponse<T> error(int code, String message, List<FieldError> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setErrors(errors);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    // 内部类：字段错误
    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;  // 错误字段名
        private String message; // 错误信息
    }
}
