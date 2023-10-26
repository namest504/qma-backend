package com.l1mit.qma_server.global.auth.oauth.service;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.PublicKeyGenerator;
import com.l1mit.qma_server.global.infra.GoogleRequester;
import com.l1mit.qma_server.global.jwt.JwtProvider;
import java.security.PublicKey;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService implements SocialAuthService {

    private final GoogleRequester googleRequester;
    private final JwtProvider jwtProvider;
    private final PublicKeyGenerator publicKeyGenerator;

    public GoogleAuthService(GoogleRequester googleRequester, JwtProvider jwtProvider,
            PublicKeyGenerator publicKeyGenerator) {
        this.googleRequester = googleRequester;
        this.jwtProvider = jwtProvider;
        this.publicKeyGenerator = publicKeyGenerator;
    }

    @Override
    public IdTokenResponse getOpenIdToken(String code, String redirectUri) {
        return googleRequester.getOpenIdToken(code, redirectUri);
    }

    @Override
    public String getAccountId(String idToken) {
        Map<String, String> headers = jwtProvider.parseHeaders(idToken);
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers,
                googleRequester.getPublicKeys());

        return jwtProvider.getTokenClaims(idToken, publicKey)
                .getSubject();
    }
}
