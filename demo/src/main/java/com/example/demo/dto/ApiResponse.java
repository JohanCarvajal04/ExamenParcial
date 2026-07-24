package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private Object meta;

    public static <T> ApiResponse<T> success(T data, String message, Object meta) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, null);
    }

    public static <T> ApiResponse<T> error(String message, Object meta) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, null);
    }
}
