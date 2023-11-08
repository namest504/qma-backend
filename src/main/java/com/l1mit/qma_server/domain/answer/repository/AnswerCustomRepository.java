package com.l1mit.qma_server.domain.answer.repository;

import com.l1mit.qma_server.domain.answer.dto.AnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerCustomRepository {

    Page<AnswerResponse> findPagedAnswerByQuestionId(Pageable pageable, Long questionId);
}
