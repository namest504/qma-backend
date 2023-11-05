package com.l1mit.qma_server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record SignInRequest(
        @JsonProperty("code")
        @NotBlank(message = "코드를 발급받고 진행해주세요.")
        String code,
        @JsonProperty("redirect_uri")
        @NotBlank(message = "redirect-uri를 설정해주세요.")
        String redirectUri
) {

        @Builder
        public SignInRequest {
        }
}
