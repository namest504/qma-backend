package com.l1mit.qma_server.global.auth.oauth.factory;

import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.service.AppleAuthService;
import com.l1mit.qma_server.global.auth.oauth.service.GoogleAuthService;
import com.l1mit.qma_server.global.auth.oauth.service.KakaoAuthService;
import com.l1mit.qma_server.global.auth.oauth.service.SocialAuthService;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SocialAuthServiceFactory {

    private final Map<SocialProvider, SocialAuthService> authServiceMap;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final GoogleAuthService googleAuthService;

    public SocialAuthServiceFactory(AppleAuthService appleAuthService,
            KakaoAuthService kakaoAuthService,
            GoogleAuthService googleAuthService) {
        authServiceMap = new EnumMap<>(SocialProvider.class);
        this.appleAuthService = appleAuthService;
        this.kakaoAuthService = kakaoAuthService;
        this.googleAuthService = googleAuthService;

        initialize();
    }

    private void initialize() {
        authServiceMap.put(SocialProvider.APPLE, appleAuthService);
        authServiceMap.put(SocialProvider.KAKAO, kakaoAuthService);
        authServiceMap.put(SocialProvider.GOOGLE, googleAuthService);
    }

    public String getAccountId(SocialProvider provider, String idToken) {
        return getAuthService(provider)
                .getAccountId(idToken);
    }

    public IdTokenResponse getSocialIdToken(SocialProvider provider, String code,
            String redirectUri) {
        return getAuthService(provider)
                .getOpenIdToken(code, redirectUri);
    }

    public SocialAuthService getAuthService(SocialProvider provider) {
        return authServiceMap.get(provider);
    }
}
