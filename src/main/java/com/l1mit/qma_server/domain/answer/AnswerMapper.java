package com.l1mit.qma_server.domain.answer;

import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.question.Question;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public Answer answerRequestToEntity(final AnswerRequest answerRequest,
            final Question question,
            final Member member) {

        return Answer.builder()
                .content(answerRequest.content())
                .member(member)
                .question(question)
                .build();
    }
}
