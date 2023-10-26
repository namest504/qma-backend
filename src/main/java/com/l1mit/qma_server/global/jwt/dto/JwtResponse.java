package com.l1mit.qma_server.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record JwtResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) {

    @Builder
    public JwtResponse {
    }
}
