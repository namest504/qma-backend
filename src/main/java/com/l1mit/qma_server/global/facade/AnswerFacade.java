package com.l1mit.qma_server.global.facade;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;

public interface AnswerFacade {

    Answer create(AnswerRequest request, Long memberId);
}
