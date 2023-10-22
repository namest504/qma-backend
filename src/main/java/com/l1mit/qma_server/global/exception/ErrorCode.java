package com.l1mit.qma_server.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    //Common
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Server Error"),
    THIRD_PARTY_API_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    NOT_SUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "지원하지 않는 Http method 입니다."),
    NOT_SUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "사용 할수 없는 MediaType 입니다."),
    INVALID_API_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터를 확인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하셨습니다."),

    TEST(HttpStatus.I_AM_A_TEAPOT, "테스트 에러");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
