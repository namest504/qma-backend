package com.l1mit.qma_server.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

public record QuestionSearchParam(
        @JsonProperty("writer")
        String writer,

        @JsonProperty("send_attitude")
        String sendAttitude,

        @JsonProperty("send_perception")
        String sendPerception,

        @JsonProperty("send_decision")
        String sendDecision,

        @JsonProperty("send_lifestyle")
        String sendLifestyle,

        @JsonProperty("receive_attitude")
        String receiveAttitude,

        @JsonProperty("receive_perception")
        String receivePerception,

        @JsonProperty("receive_decision")
        String receiveDecision,

        @JsonProperty("receive_lifestyle")
        String receiveLifestyle,

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
