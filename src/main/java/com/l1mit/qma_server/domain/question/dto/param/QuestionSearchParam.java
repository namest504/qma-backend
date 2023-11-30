package com.l1mit.qma_server.domain.question.dto.param;

import static com.l1mit.qma_server.global.constants.RegexpConstants.REGEXP_MBTI;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.MBTI_MISMATCH;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

public record QuestionSearchParam(
        String writer,

        @Pattern(regexp = REGEXP_MBTI, message = MBTI_MISMATCH)
        String sendMbti,

        String receiveMbti,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startTime,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endTime
) {

    @Builder
    public QuestionSearchParam {
    }
}
