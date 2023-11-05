package com.l1mit.qma_server.domain.question.dto;

import lombok.Builder;

public record QuestionRequest(
        String content,
        String attitude,
        String perception,
        String decision,
        String lifestyle
) {

    @Builder
    public QuestionRequest {
    }
}
