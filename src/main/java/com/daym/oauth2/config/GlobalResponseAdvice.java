package com.daym.oauth2.config;
import com.daym.oauth2.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否支持该返回类型的处理，通常可以返回 true 来对所有返回类型进行处理。
     * 如果你需要对某些返回类型或条件做过滤，可以在这里做判断。
     */

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 在响应体返回之前执行的操作，可以在这里对响应体进行封装。
     * 比如将所有的响应封装成统一格式：{msg: "Success", status: 200, data: ...}
     */



    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回值为 null，统一处理为成功响应
        if (body == null) {
            return ApiResponse.success(null);  // 返回空的数据部分，但状态为200
        }

        // 如果返回值是一个普通类型的数据，直接封装成 ApiResponse
        return ApiResponse.success(body);
    }
}
