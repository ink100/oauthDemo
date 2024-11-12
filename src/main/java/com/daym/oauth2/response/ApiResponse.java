package com.daym.oauth2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(data);
        return response;
    }

    // 失败响应
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
