package com.l1mit.qma_server.global.facade;

import com.l1mit.qma_server.domain.answer.Answer;
import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;

public interface AnswerFacade {

    Answer create(AnswerRequest request, Long memberId);
}
