package com.l1mit.qma_server.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdTokenResponse(
        @JsonProperty("id_token")
        String idToken
) {

}
