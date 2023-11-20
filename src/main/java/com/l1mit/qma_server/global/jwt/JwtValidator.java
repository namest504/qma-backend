package com.l1mit.qma_server.global.jwt;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.global.auth.oauth.dto.PrincipalUser;
import com.l1mit.qma_server.global.auth.oauth.factory.SocialAuthServiceFactory;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    private final SocialAuthServiceFactory socialAuthServiceFactory;
    private final MemberRepository memberRepository;

    public JwtValidator(final SocialAuthServiceFactory socialAuthServiceFactory, final MemberRepository memberRepository) {
        this.socialAuthServiceFactory = socialAuthServiceFactory;
        this.memberRepository = memberRepository;
    }

    public Authentication getAuthentication(final String idToken, final SocialProvider socialProvider) {

        String accountId = socialAuthServiceFactory.getAccountId(socialProvider, idToken);
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new QmaApiException(ErrorCode.UNAUTHORIZED));
        PrincipalUser principalUser = PrincipalUser.builder()
                .member(member)
                .build();

        return new UsernamePasswordAuthenticationToken(principalUser, "",
                principalUser.getAuthorities());
    }

}
