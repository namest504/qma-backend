package com.l1mit.qma_server.domain.question.repository;


import static com.l1mit.qma_server.domain.question.domain.QQuestion.question;

import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QuestionCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<QuestionResponse> searchWithCondition(final Pageable pageable, final QuestionSearchParam param) {

        List<QuestionResponse> content = jpaQueryFactory.select(Projections.constructor(QuestionResponse.class,
                        question.id.as("id"),
                        question.member.nickname.as("writer"),
                        question.receiveMbtiEntity.mbti.as("mbti"),
                        question.auditEntity.createdAt.as("createdAt")
                ))
                .from(question)
                .where(questionWriterNicknameEq(param.writer()),
                        questionReceiverMbtiEq(param.receiveMbti()),
                        questionSenderMbtiEq(param.sendMbti()),
                        questionCreatedAtBetween(param.startTime(), param.endTime()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(question.count())
                .from(question)
                .where(questionWriterNicknameEq(param.writer()),
                        questionReceiverMbtiEq(param.receiveMbti()),
                        questionSenderMbtiEq(param.sendMbti()),
                        questionCreatedAtBetween(param.startTime(), param.endTime()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<QuestionDetailResponse> findByIdWithDetail(final Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.select(Projections.constructor(QuestionDetailResponse.class,
                                question.id.as("id"),
                                question.member.nickname.as("writer"),
                                question.receiveMbtiEntity.mbti.as("mbti"),
                                question.content.as("content"),
                                question.auditEntity.createdAt.as("createdAt")
                        ))
                        .from(question)
                        .where(question.id.eq(id))
                        .fetchOne()
        );
    }

    private BooleanExpression questionWriterNicknameEq(final String nickname) {
            return nickname != null ? question.member.nickname.eq(nickname) : null;
    }
    private BooleanExpression questionSenderMbtiEq(final String mbti) {
        return mbti != null ? question.member.mbtiEntity.mbti.eq(MBTI.valueOf(mbti)) : null;
    }
    private BooleanExpression questionReceiverMbtiEq(final String mbti) {
        return mbti != null ? question.receiveMbtiEntity.mbti.eq(MBTI.valueOf(mbti)) : null;
    }
    private BooleanExpression questionCreatedAtBetween(final LocalDate start, final LocalDate end) {
        return ( start != null && end != null )?
                question.auditEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX)) : null;
    }
}
