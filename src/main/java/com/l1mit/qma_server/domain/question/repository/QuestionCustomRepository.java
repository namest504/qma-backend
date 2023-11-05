package com.l1mit.qma_server.domain.question.repository;

import com.l1mit.qma_server.domain.question.dto.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionSearchParam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface QuestionCustomRepository {

    List<QuestionResponse> searchWithCondition(Pageable pageable, QuestionSearchParam param);

    Optional<QuestionDetailResponse> findByIdWithDetail(Long id);
}
