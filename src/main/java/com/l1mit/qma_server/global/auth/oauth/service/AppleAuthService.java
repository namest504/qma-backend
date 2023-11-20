package com.l1mit.qma_server.global.auth.oauth.service;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.PublicKeyGenerator;
import com.l1mit.qma_server.global.infra.AppleRequester;
import com.l1mit.qma_server.global.jwt.JwtProvider;
import java.security.PublicKey;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AppleAuthService implements SocialAuthService {

    private final AppleRequester appleRequester;
    private final JwtProvider jwtProvider;
    private final PublicKeyGenerator publicKeyGenerator;

    public AppleAuthService(
            final AppleRequester appleRequester,
            final JwtProvider jwtProvider,
            final PublicKeyGenerator publicKeyGenerator) {
        this.appleRequester = appleRequester;
        this.jwtProvider = jwtProvider;
        this.publicKeyGenerator = publicKeyGenerator;
    }

    @Override
    public IdTokenResponse getOpenIdToken(final String code, final String redirectUri) {
        return appleRequester.getOpenIdToken(code, redirectUri);
    }

    @Override
    public String getAccountId(final String idToken) {
        Map<String, String> headers = jwtProvider.parseHeaders(idToken);
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers,
                appleRequester.getPublicKeys());

        return jwtProvider.getTokenClaims(idToken, publicKey)
                .getSubject();
    }


}
