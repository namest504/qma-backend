package com.l1mit.qma_server.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    //Common
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Server Error"),
    THIRD_PARTY_API_EXCEPTION(HttpStatus.BAD_REQUEST, "외부 API 요청 문제 발생"),
    NOT_SUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "지원하지 않는 Http method 입니다."),
    NOT_SUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "사용 할수 없는 MediaType 입니다."),
    INVALID_API_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터를 확인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하셨습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정보입니다."),
    JSON_PROCESSING(HttpStatus.BAD_REQUEST, "JSON 변환 중 에러가 발생했습니다."),
    FAILED_OIDC_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "OIDC 공개키 생성에 문제가 발생했습니다."),
    NOT_MATCHED_MBTI(HttpStatus.BAD_REQUEST, "대상 MBTI와 일치하지 않습니다."),
    NOT_WRITER(HttpStatus.BAD_REQUEST, "해당 질문의 질문자가 아닙니다."),
    NOT_RESPONDENT(HttpStatus.BAD_REQUEST, "해당 대답의 대답자가 아닙니다."),
    ALREADY_MEMBER_IN_ROOM(HttpStatus.BAD_REQUEST, "이미 방에 접속되어있는 유저입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
