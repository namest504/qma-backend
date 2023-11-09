package com.l1mit.qma_server.domain.question.service;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.domain.question.mapper.QuestionMapper;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(MemberService memberService, QuestionRepository questionRepository,
            QuestionMapper questionMapper) {
        this.memberService = memberService;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Transactional
    public Question save(Long memberId, QuestionRequest request) {
        Member member = memberService.findById(memberId);
        return questionRepository.save(questionMapper.questionRequestToEntity(request, member));
    }

    @Transactional(readOnly = true)
    public Page<QuestionResponse> searchWithCondition(Pageable pageable,
            QuestionSearchParam param) {
        return questionRepository.searchWithCondition(pageable, param);
    }

    public QuestionDetailResponse getDetail(Long id) {
        return questionRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }
}
