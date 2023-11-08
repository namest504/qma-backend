package com.l1mit.qma_server.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;

public record QuestionDetailResponse(
        @JsonProperty("writer")
        String writer,

        @JsonProperty("attitude")
        String attitude,

        @JsonProperty("perception")
        String perception,

        @JsonProperty("decision")
        String decision,

        @JsonProperty("lifestyle")
        String lifestyle,

        @JsonProperty("content")
        String content,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    @Builder
    public QuestionDetailResponse {
    }
}
