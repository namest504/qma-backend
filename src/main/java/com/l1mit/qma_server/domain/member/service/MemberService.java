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

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public MemberInfoResponse findMemberInfoResponseById(Long id) {
        Member findedMember = memberRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
        return memberMapper.entityToMemberInfoResponse(findedMember);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<Member> findOauth2Account(SocialProvider provider, String accountId) {
        return memberRepository.findByOauth2AccountId(provider, accountId);
    }

    public Member save(Oauth2Entity oauth2Entity) {
        return memberRepository.save(new Member(oauth2Entity));
    }
}
