package com.l1mit.qma_server.global.common.response;

import lombok.Builder;

public record PageResponse<T>(
        T content,
        Integer totalElement,
        Integer totalPage
) {

    @Builder
    public PageResponse {
    }

    public static <T> PageResponse<T> create(T data, Integer totalElement, Integer totalPage) {
        return new PageResponse<>(data, totalElement, totalPage);
    }
}
