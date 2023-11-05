package com.l1mit.qma_server.domain.question.repository;

import com.l1mit.qma_server.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>,
        QuestionCustomRepository {

}
