package com.l1mit.qma_server.domain.question.dto.request;

import static com.l1mit.qma_server.global.constants.RegexpConstants.REGEXP_MBTI;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.MBTI_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.QUESTION_CONTENT_NOT_BLANK;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record QuestionRequest(
        @JsonProperty("content")
        @NotBlank(message = QUESTION_CONTENT_NOT_BLANK)
        String content,

        @JsonProperty("mbti")
        @Pattern(regexp = REGEXP_MBTI, message = MBTI_MISMATCH)
        String mbti
) {

    @Builder
    public QuestionRequest {
    }
}
