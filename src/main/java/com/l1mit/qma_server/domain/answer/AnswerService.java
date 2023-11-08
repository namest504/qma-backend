package com.l1mit.qma_server.domain.answer;

import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;
import com.l1mit.qma_server.domain.answer.repository.AnswerRepository;
import com.l1mit.qma_server.global.facade.AnswerFacade;
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
}
