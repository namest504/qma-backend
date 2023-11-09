package com.l1mit.qma_server.domain.question.repository;

import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionCustomRepository {

    Page<QuestionResponse> searchWithCondition(Pageable pageable, QuestionSearchParam param);

    Optional<QuestionDetailResponse> findByIdWithDetail(Long id);
}
