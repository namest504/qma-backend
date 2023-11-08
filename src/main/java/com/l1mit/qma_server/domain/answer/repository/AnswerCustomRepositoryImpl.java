package com.l1mit.qma_server.domain.answer.repository;

import static com.l1mit.qma_server.domain.answer.QAnswer.answer;

import com.l1mit.qma_server.domain.answer.dto.AnswerResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AnswerCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<AnswerResponse> findPagedAnswerByQuestionId(Pageable pageable, Long questionId) {

        BooleanBuilder builder = new BooleanBuilder();
        if (questionId != null) {
            builder.and(answer.question.id.eq(questionId));
        }

        List<AnswerResponse> result = jpaQueryFactory.select(
                        Projections.constructor(AnswerResponse.class,
                                answer.member.nickname.as("writer"),
                                answer.content.as("content"),
                                answer.auditEntity.createdAt.as("createdAt")))
                .from(answer)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                        answer.count())
                .from(answer)
                .where(builder);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
