package com.l1mit.qma_server.domain.answer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;

public record AnswerResponse(
        @JsonProperty("writer")
        String writer,

        @JsonProperty("content")
        String content,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    @Builder
    public AnswerResponse {
    }
}
