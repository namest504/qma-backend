package com.l1mit.qma_server.global.exception;

import lombok.Getter;

@Getter
public class QmaApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public QmaApiException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
