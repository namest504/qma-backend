package com.l1mit.qma_server.domain.answer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AnswerCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
