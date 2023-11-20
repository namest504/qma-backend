package com.l1mit.qma_server.domain.member.repository;

import static com.l1mit.qma_server.domain.member.domain.QMember.member;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;

public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Member> findByOauth2AccountId(final SocialProvider socialProvider, final String accountId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .where(member.oauth2Entity.socialProvider.eq(socialProvider),
                                member.oauth2Entity.accountId.eq(accountId))
                        .fetchOne());
    }

    @Override
    public Optional<Member> findByAccountId(final String accountId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(member)
                        .where(member.oauth2Entity.accountId.eq(accountId))
                        .fetchOne());
    }
}
