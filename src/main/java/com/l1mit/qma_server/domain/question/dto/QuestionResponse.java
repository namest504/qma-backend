package com.l1mit.qma_server.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

public record QuestionResponse(
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
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt
) {

    @Builder
    public QuestionResponse {
    }
}
