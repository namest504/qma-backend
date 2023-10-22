package com.l1mit.qma_server.global.exception;

import com.l1mit.qma_server.global.common.response.ApiResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    // Api 비즈니스 로직 예외
    @ExceptionHandler(QmaApiException.class)
    public ResponseEntity<ApiResponse<?>> QmaApiExceptionHandler(
            final QmaApiException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(ApiResponse.createFail(exception.getErrorCode()));
    }

    // Http Request Method 예외
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createFail(ErrorCode.NOT_SUPPORTED_METHOD));
    }

    // API 요청 파라미터 값의 유효성 위반
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> badRequestExHandler(BindingResult bindingResult) {
        String msg = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createFailWithInput(
                        msg));
    }

    // 알 수 없는 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> unhandledExceptionHandler(final Exception exception) {
        log.warn(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.createFail(ErrorCode.SERVER_ERROR));
    }
}
