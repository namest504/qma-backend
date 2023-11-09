package com.l1mit.qma_server.global.facade;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.mapper.QuestionMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionFacadeImpl implements QuestionFacade {

    private final MemberService memberService;
    private final QuestionMapper questionMapper;

    public QuestionFacadeImpl(MemberService memberService, QuestionMapper questionMapper) {
        this.memberService = memberService;
        this.questionMapper = questionMapper;
    }

    @Override
    public Question create(QuestionRequest questionRequest, Long memberId) {
        Member member = memberService.findById(memberId);
        return questionMapper.questionRequestToEntity(questionRequest, member);
    }
}
