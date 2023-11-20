package com.l1mit.qma_server.domain.member.service;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.dto.response.MemberInfoResponse;
import com.l1mit.qma_server.domain.member.mapper.MemberMapper;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(final MemberRepository memberRepository, final MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public MemberInfoResponse findMemberInfoResponseById(final Long id) {
        return memberMapper.entityToMemberInfoResponse(findById(id));
    }

    public Member findById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<Member> findOauth2Account(final SocialProvider provider, final String accountId) {
        return memberRepository.findByOauth2AccountId(provider, accountId);
    }

    public Member save(final Oauth2Entity oauth2Entity) {
        return memberRepository.save(new Member(oauth2Entity));
    }
}
