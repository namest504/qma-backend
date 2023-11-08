package com.l1mit.qma_server.global.facade;

import com.l1mit.qma_server.domain.answer.Answer;
import com.l1mit.qma_server.domain.answer.AnswerMapper;
import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;
import com.l1mit.qma_server.domain.member.MemberService;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.question.Question;
import com.l1mit.qma_server.domain.question.QuestionService;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AnswerFacadeImpl implements AnswerFacade {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerMapper answerMapper;

    public AnswerFacadeImpl(MemberService memberService, QuestionService questionService,
            AnswerMapper answerMapper) {
        this.memberService = memberService;
        this.questionService = questionService;
        this.answerMapper = answerMapper;
    }

    @Override
    @Transactional
    public Answer create(AnswerRequest request, Long memberId) {
        Member findedMember = memberService.findById(memberId);
        Question findedQuestion = questionService.findById(request.questionId());
        validateMatchingMbti(findedQuestion, findedMember);
        return answerMapper.answerRequestToEntity(request, findedQuestion, findedMember);
    }

    private void validateMatchingMbti(Question question, Member member) {
        if (!question.getReceiveMbtiEntity().equals(member.getMbtiEntity())) {
            throw new QmaApiException(ErrorCode.NOT_MATCHED_MBTI);
        }
    }
}
