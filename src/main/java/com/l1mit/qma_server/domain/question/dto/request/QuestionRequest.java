package com.l1mit.qma_server.domain.question.dto.request;

import static com.l1mit.qma_server.global.constants.RegexpConstants.EorI;
import static com.l1mit.qma_server.global.constants.RegexpConstants.NorS;
import static com.l1mit.qma_server.global.constants.RegexpConstants.PorJ;
import static com.l1mit.qma_server.global.constants.RegexpConstants.TorF;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ATTITUDE_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.DECISION_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.LIFESTYLE_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.PERCEPTION_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.QUESTION_CONTENT_NOT_BLANK;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record QuestionRequest(
        @JsonProperty("content")
        @NotBlank(message = QUESTION_CONTENT_NOT_BLANK)
        String content,

        @JsonProperty("attitude")
        @Pattern(regexp = EorI, message = ATTITUDE_MISMATCH)
        String attitude,

        @JsonProperty("perception")
        @Pattern(regexp = NorS, message = PERCEPTION_MISMATCH)
        String perception,

        @JsonProperty("decision")
        @Pattern(regexp = TorF, message = DECISION_MISMATCH)
        String decision,

        @JsonProperty("lifestyle")
        @Pattern(regexp = PorJ, message = LIFESTYLE_MISMATCH)
        String lifestyle
) {

    @Builder
    public QuestionRequest {
    }
}
