package com.l1mit.qma_server.domain.answer.mapper;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.question.domain.Question;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public Answer answerRequestToEntity(AnswerRequest answerRequest,
            Question question,
            Member member) {

        return Answer.builder()
                .content(answerRequest.content())
                .member(member)
                .question(question)
                .build();
    }
}
