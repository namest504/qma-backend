package com.l1mit.qma_server.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record QuestionRequest(
        @JsonProperty("content")
        String content,

        @JsonProperty("attitude")
        String attitude,

        @JsonProperty("perception")
        String perception,

        @JsonProperty("decision")
        String decision,

        @JsonProperty("lifestyle")
        String lifestyle
) {

    @Builder
    public QuestionRequest {
    }
}
