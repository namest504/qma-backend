package com.l1mit.qma_server.domain.answer.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ANSWER_CONTENT_NOT_BLANK;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.QUESTION_ID_NOT_NULL;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record AnswerRequest(
        @JsonProperty("content")
        @NotBlank(message = ANSWER_CONTENT_NOT_BLANK)
        String content,

        @JsonProperty("question_id")
        @NotNull(message = QUESTION_ID_NOT_NULL)

        Long questionId) {

    @Builder
    public AnswerRequest {
    }
}
