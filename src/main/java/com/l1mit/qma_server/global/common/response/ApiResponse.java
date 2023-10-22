package com.l1mit.qma_server.global.common.response;

import com.l1mit.qma_server.global.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;

public record ApiResponse<T>(
        T data,
        String message,
        LocalDateTime timestamp
) {

    @Builder
    public ApiResponse {
    }

    public static <T> ApiResponse<T> createSuccessWithData(T data) {
        return new ApiResponse<>(data, null, getCurrentTime());
    }

    public static <T> ApiResponse<?> createSuccess() {
        return new ApiResponse<>(null, null, getCurrentTime());
    }

    public static <T> ApiResponse<T> createFailWithInput(
            String message) {
        return new ApiResponse<>(null, message, getCurrentTime());
    }

    public static ApiResponse<?> createFail(ErrorCode errorCode) {
        return new ApiResponse<>(null, errorCode.getMessage(), getCurrentTime());
    }

    private static LocalDateTime getCurrentTime() {
        return LocalDateTime.now().withNano(0);
    }
}
