package com.l1mit.qma_server.domain.question.mapper;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question questionRequestToEntity(QuestionRequest request, Member member) {
        return Question.builder()
                .member(member)
                .content(request.content())
                .receiveMbtiEntity(MbtiEntity.builder()
                        .attitude(request.attitude())
                        .perception(request.perception())
                        .decision(request.decision())
                        .lifestyle(request.lifestyle())
                        .build())
                .build();
    }
}
