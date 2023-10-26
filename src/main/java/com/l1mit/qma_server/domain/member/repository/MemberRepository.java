package com.l1mit.qma_server.domain.member.repository;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByOauth2AccountId(SocialProvider socialProvider, String accountId);

    Member save(Member member);
}
