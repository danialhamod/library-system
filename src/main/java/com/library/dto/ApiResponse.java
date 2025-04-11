package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Integer errorCode;
    private T data;

    // Static factory methods
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Operation successful");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(String message, Integer errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrorCode(errorCode);
        return response;
    }

    // Builder-style methods for chaining
    public ApiResponse<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> withErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ApiResponse<T> withData(T data) {
        this.data = data;
        return this;
    }
}