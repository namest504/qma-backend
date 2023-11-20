package com.l1mit.qma_server.domain.answer.service;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;
import com.l1mit.qma_server.domain.answer.dto.response.AnswerResponse;
import com.l1mit.qma_server.domain.answer.repository.AnswerRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.facade.AnswerFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerFacade answerFacade;

    public AnswerService(AnswerRepository answerRepository, AnswerFacade answerFacade) {
        this.answerRepository = answerRepository;
        this.answerFacade = answerFacade;
    }

    @Transactional
    public void create(AnswerRequest request, Long memberId) {
        Answer answer = answerFacade.create(request, memberId);
        answerRepository.save(answer);
    }

    public Page<AnswerResponse> findPagedAnswerByQuestionId(Pageable pageable, Long questionId) {
        return answerRepository.findPagedAnswerByQuestionId(pageable, questionId);
    }

    @Transactional(readOnly = true)
    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public void deleteById(Long answerId, Long memberId) {
        Answer answer = getAnswerById(answerId);
        if (!validateAnswerRespondent(memberId, answer)) {
            throw new QmaApiException(ErrorCode.NOT_RESPONDENT);
        }
        answerRepository.deleteById(answerId);
    }

    private Boolean validateAnswerRespondent(Long memberId, Answer answer) {
        return answer.getMember().getId().equals(memberId);
    }
}
