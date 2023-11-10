package com.l1mit.qma_server.global.auth;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.dto.request.SignInRequest;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.factory.SocialAuthServiceFactory;
import com.l1mit.qma_server.global.auth.oauth.service.SocialAuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SocialAuthServiceFactory socialAuthServiceFactory;
    private final MemberService memberService;

    public AuthService(SocialAuthServiceFactory socialAuthServiceFactory,
            MemberService memberService) {
        this.socialAuthServiceFactory = socialAuthServiceFactory;
        this.memberService = memberService;
    }

    public IdTokenResponse signIn(String provider, SignInRequest request) {
        SocialProvider socialProvider = getSocialProvider(provider);
        SocialAuthService socialAuthService = socialAuthServiceFactory.getAuthService(
                socialProvider);

        IdTokenResponse idTokenResponse = socialAuthService.getOpenIdToken(request.code(),
                request.redirectUri());

        String accountId = socialAuthService.getAccountId(idTokenResponse.idToken());

        Member oauth2Account = memberService.findOauth2Account(socialProvider, accountId)
                .orElseGet(() -> memberService.save(
                        Oauth2Entity.builder()
                                .socialProvider(socialProvider)
                                .accountId(accountId)
                                .build()));

        return idTokenResponse;
    }

    private SocialProvider getSocialProvider(String provider) {
        return SocialProvider.valueOf(provider);
    }
}
