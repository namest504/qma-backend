package com.l1mit.qma_server.global.auth.oauth.service;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.PublicKeyGenerator;
import com.l1mit.qma_server.global.infra.KakaoRequester;
import com.l1mit.qma_server.global.jwt.JwtProvider;
import java.security.PublicKey;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService implements SocialAuthService {

    private final KakaoRequester kakaoRequester;
    private final JwtProvider jwtProvider;
    private final PublicKeyGenerator publicKeyGenerator;

    public KakaoAuthService(KakaoRequester kakaoRequester, JwtProvider jwtProvider,
            PublicKeyGenerator publicKeyGenerator) {
        this.kakaoRequester = kakaoRequester;
        this.jwtProvider = jwtProvider;
        this.publicKeyGenerator = publicKeyGenerator;
    }

    @Override
    public IdTokenResponse getOpenIdToken(String code, String redirectUri) {
        return kakaoRequester.getOpenIdToken(code, redirectUri);
    }

    @Override
    public String getAccountId(String idToken) {
        Map<String, String> headers = jwtProvider.parseHeaders(idToken);
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers,
                kakaoRequester.getPublicKeys());

        return jwtProvider.getTokenClaims(idToken, publicKey)
                .getSubject();
    }
}
