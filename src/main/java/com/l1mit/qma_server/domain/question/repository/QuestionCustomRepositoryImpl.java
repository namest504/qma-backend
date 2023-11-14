package com.l1mit.qma_server.domain.question.repository;


import static com.l1mit.qma_server.domain.question.domain.QQuestion.question;

import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QuestionCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<QuestionResponse> searchWithCondition(Pageable pageable,
            QuestionSearchParam param) {
        BooleanBuilder builder = getBooleanBuilder(param);

        List<QuestionResponse> content = jpaQueryFactory.select(Projections.constructor(QuestionResponse.class,
                        question.id.as("id"),
                        question.member.nickname.as("writer"),
                        question.receiveMbtiEntity.mbti.as("mbti"),
                        question.auditEntity.createdAt.as("createdAt")
                ))
                .from(question)
                .where(builder)
                /* todo: 메소드로 뺄지 builder 그대로 넣을지 고민
                .where(questionWriterNicknameEq(param.writer()),
                        questionReceiverMbtiEq(param.receiveMbti()),
                        questionSenderMbtiEq(param.sendMbti()),
                        questionCreatedAtBetween(param.startTime(), param.endTime()))*/
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(question.count())
                .from(question)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<QuestionDetailResponse> findByIdWithDetail(Long id) {
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

    private BooleanBuilder getBooleanBuilder(QuestionSearchParam param) {
        BooleanBuilder builder = new BooleanBuilder();
        if (param.writer() != null) {
            builder.and(question.member.nickname.eq(param.writer()));
        }
        if (param.sendMbti() != null) {
            builder.and(question.member.mbtiEntity.mbti.eq(MBTI.valueOf(param.sendMbti())));
        }
        if (param.receiveMbti() != null) {
            builder.and(question.receiveMbtiEntity.mbti.eq(MBTI.valueOf(param.receiveMbti())));
        }
        if (param.startTime() != null && param.endTime() != null) {
            builder.and(question.auditEntity.createdAt.between(
                    param.startTime().atStartOfDay(),
                    param.endTime().atTime(LocalTime.MAX))
            );
        }
        return builder;
    }
    /* todo: 메소드로 뺄지 builder 그대로 넣을지 고민
    private BooleanExpression questionWriterNicknameEq(String nickname) {
            return nickname != null ? question.member.nickname.eq(nickname) : null;
    }
    private BooleanExpression questionSenderMbtiEq(String mbti) {
        return mbti != null ? question.member.mbtiEntity.mbti.eq(MBTI.valueOf(mbti)) : null;
    }
    private BooleanExpression questionReceiverMbtiEq(String mbti) {
        return mbti != null ? question.receiveMbtiEntity.mbti.eq(MBTI.valueOf(mbti)) : null;
    }
    private BooleanExpression questionCreatedAtBetween(LocalDate start, LocalDate end) {
        return ( start != null && end != null )?
                question.auditEntity.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX)) : null;
    }*/
}
