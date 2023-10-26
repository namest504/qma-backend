package com.l1mit.qma_server.global.auth.oauth.service;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;

public interface SocialAuthService {

    IdTokenResponse getOpenIdToken(String code, String redirectUri);

    String getAccountId(String idToken);
}
