package com.l1mit.qma_server.global.infra;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.OidcPublicKeyResponse;

public interface OauthAPIRequester {

    IdTokenResponse getOpenIdToken(String code, String redirectUri);

    OidcPublicKeyResponse getPublicKeys();
}
