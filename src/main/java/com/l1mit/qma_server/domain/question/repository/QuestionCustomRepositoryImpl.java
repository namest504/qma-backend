package com.l1mit.qma_server.domain.question.repository;


import static com.l1mit.qma_server.domain.question.QQuestion.question;

import com.l1mit.qma_server.domain.member.domain.enums.mbti.Attitude;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Decision;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Lifestyle;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Perception;
import com.l1mit.qma_server.domain.question.dto.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionSearchParam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public QuestionCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<QuestionResponse> searchWithCondition(Pageable pageable,
            QuestionSearchParam param) {
        BooleanBuilder builder = getBooleanBuilder(param);

        return jpaQueryFactory.select(Projections.constructor(QuestionResponse.class,
                        question.id.as("id"),
                        question.member.nickname.as("writer"),
                        question.receiveMbtiEntity.attitude.as("attitude"),
                        question.receiveMbtiEntity.perception.as("perception"),
                        question.receiveMbtiEntity.decision.as("decision"),
                        question.receiveMbtiEntity.lifestyle.as("lifestyle"),
                        question.auditEntity.createdAt.as("created_at")
                ))
                .from(question)
                .where(builder)
                .fetch();
    }

    @Override
    public Optional<QuestionDetailResponse> findByIdWithDetail(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.select(Projections.constructor(QuestionDetailResponse.class,
                                question.id.as("id"),
                                question.member.nickname.as("writer"),
                                question.receiveMbtiEntity.attitude.as("attitude"),
                                question.receiveMbtiEntity.perception.as("perception"),
                                question.receiveMbtiEntity.decision.as("decision"),
                                question.receiveMbtiEntity.lifestyle.as("lifestyle"),
                                question.content.as("content"),
                                question.auditEntity.createdAt.as("created_at")
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
        if (param.sendAttitude() != null
                && param.sendPerception() != null
                && param.sendDecision() != null
                && param.sendLifestyle() != null) {
            builder.and(question.member.mbtiEntity.attitude.eq(
                            Attitude.valueOf(param.sendAttitude())))
                    .and(question.member.mbtiEntity.perception.eq(
                            Perception.valueOf(param.sendPerception())))
                    .and(question.member.mbtiEntity.decision.eq(
                            Decision.valueOf(param.sendDecision())))
                    .and(question.member.mbtiEntity.lifestyle.eq(
                            Lifestyle.valueOf(param.sendLifestyle())));
        }
        if (param.receiveAttitude() != null
                && param.receivePerception() != null
                && param.receiveDecision() != null
                && param.receiveLifestyle() != null) {
            builder.and(question.receiveMbtiEntity.attitude.eq(
                            Attitude.valueOf(param.receiveAttitude())))
                    .and(question.receiveMbtiEntity.perception.eq(
                            Perception.valueOf(param.receivePerception())))
                    .and(question.receiveMbtiEntity.decision.eq(
                            Decision.valueOf(param.receiveDecision())))
                    .and(question.receiveMbtiEntity.lifestyle.eq(
                            Lifestyle.valueOf(param.receiveAttitude())));
        }
        if (param.startTime() != null && param.endTime() != null) {
            builder.and(question.auditEntity.createdAt.between(param.startTime().atStartOfDay(),
                    param.endTime().atStartOfDay()));
        }
        return builder;
    }
}
