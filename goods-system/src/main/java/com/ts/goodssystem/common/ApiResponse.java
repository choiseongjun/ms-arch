package com.ts.goodssystem.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Status status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(Status.SUCCESS, null, "Success");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Status.SUCCESS, data, "Success");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(Status.SUCCESS, data, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(Status.ERROR, null, message);
    }

    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>(Status.ERROR, data, message);
    }

    public enum Status {
        SUCCESS, ERROR
    }
} 