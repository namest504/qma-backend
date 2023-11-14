package com.l1mit.qma_server.domain.question.dto.param;

import static com.l1mit.qma_server.global.constants.RegexpConstants.REGEXP_MBTI;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.MBTI_MISMATCH;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

public record QuestionSearchParam(
        @JsonProperty("writer")
        String writer,

        @JsonProperty("send_mbti")
        @Pattern(regexp = REGEXP_MBTI, message = MBTI_MISMATCH)
        String sendMbti,

        @JsonProperty("receive_mbti")
        String receiveMbti,

        @JsonProperty("start_time")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startTime,

        @JsonProperty("end_time")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endTime
) {

    @Builder
    public QuestionSearchParam {
    }
}
