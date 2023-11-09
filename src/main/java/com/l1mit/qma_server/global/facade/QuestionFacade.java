package com.l1mit.qma_server.global.facade;

import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;

public interface QuestionFacade {

    Question create(QuestionRequest questionRequest, Long memberId);
}
