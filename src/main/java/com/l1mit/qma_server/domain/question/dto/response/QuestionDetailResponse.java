package com.l1mit.qma_server.domain.question.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.l1mit.qma_server.global.common.domain.MBTI;
import java.time.LocalDateTime;
import lombok.Builder;

public record QuestionDetailResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("writer")
        String writer,

        @JsonProperty("mbti")
        MBTI mbti,

        @JsonProperty("content")
        String content,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    @Builder
    public QuestionDetailResponse {
    }
}
