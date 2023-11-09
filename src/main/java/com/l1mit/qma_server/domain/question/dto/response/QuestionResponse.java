package com.l1mit.qma_server.domain.question.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;

public record QuestionResponse(
        @JsonProperty("id")
        Long id,

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

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    @Builder
    public QuestionResponse {
    }
}
