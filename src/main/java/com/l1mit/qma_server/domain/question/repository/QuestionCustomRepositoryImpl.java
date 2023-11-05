package com.l1mit.qma_server.domain.question.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QuestionCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

}
