package com.l1mit.qma_server.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record IdTokenResponse(
        @JsonProperty("id_token")
        String idToken
) {

        @Builder
        public IdTokenResponse {
        }
}
