package com.l1mit.qma_server.domain.answer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record AnswerRequest(
        @JsonProperty("content")
        String content,

        @JsonProperty("question_id")
        Long questionId) {

    @Builder
    public AnswerRequest {
    }
}
