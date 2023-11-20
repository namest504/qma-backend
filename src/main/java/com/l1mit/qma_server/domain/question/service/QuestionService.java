package com.l1mit.qma_server.domain.question.service;

import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.facade.QuestionFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionFacade questionFacade;

    public QuestionService(final QuestionRepository questionRepository, final QuestionFacade questionFacade) {
        this.questionRepository = questionRepository;
        this.questionFacade = questionFacade;
    }

    @Transactional
    public Question save(final Long memberId, final QuestionRequest request) {

        Question question = questionFacade.create(request, memberId);

        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionResponse> searchWithCondition(final Pageable pageable, final QuestionSearchParam param) {
        return questionRepository.searchWithCondition(pageable, param);
    }

    public QuestionDetailResponse getDetail(final Long id) {
        return questionRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public Question findById(final Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public void deleteById(final Long questionId, final Long memberId) {

        Question question = findById(questionId);

        if (!validateQuestionWriter(memberId, question)) {
            throw new QmaApiException(ErrorCode.NOT_WRITER);
        }

        questionRepository.deleteById(questionId);

    }

    private Boolean validateQuestionWriter(final Long memberId, final Question question) {
        return question.getMember().getId().equals(memberId);
    }
}
