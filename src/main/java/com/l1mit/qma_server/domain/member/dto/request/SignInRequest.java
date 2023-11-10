package com.l1mit.qma_server.domain.member.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.CODE_REQUIRE;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.REDIRECT_URI_REQUIRE;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record SignInRequest(
        @JsonProperty("code")
        @NotBlank(message = CODE_REQUIRE)
        String code,

        @JsonProperty("redirect_uri")
        @NotBlank(message = REDIRECT_URI_REQUIRE)
        String redirectUri
) {

        @Builder
        public SignInRequest {
        }
}
