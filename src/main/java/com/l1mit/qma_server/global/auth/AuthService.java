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

    public AuthService(
            final SocialAuthServiceFactory socialAuthServiceFactory,
            final MemberService memberService) {
        this.socialAuthServiceFactory = socialAuthServiceFactory;
        this.memberService = memberService;
    }

    public IdTokenResponse signIn(final String provider, final SignInRequest request) {

        SocialProvider socialProvider = getSocialProvider(provider);

        IdTokenResponse idTokenResponse = socialAuthServiceFactory.getSocialIdToken(socialProvider,request);

        String accountId = socialAuthServiceFactory.getAccountId(socialProvider, idTokenResponse.idToken());

        memberService.findOauth2Account(socialProvider, accountId)
                .orElseGet(() -> memberService.save(
                        Oauth2Entity.builder()
                                .socialProvider(socialProvider)
                                .accountId(accountId)
                                .build()));

        return idTokenResponse;
    }

    private SocialProvider getSocialProvider(final String provider) {
        return SocialProvider.valueOf(provider);
    }
}
