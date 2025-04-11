package com.library.config;

import com.library.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "com.library.controller")
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, 
                          Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // Wrap ALL successful responses
    }

    @Override
    public Object beforeBodyWrite(Object body, 
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, 
                                ServerHttpResponse response) {
        
        // Skip if already wrapped or is error response
        if (body instanceof ApiResponse) {
            return body;
        }

        // Default wrapping
        return ApiResponse.success(body);
    }
}