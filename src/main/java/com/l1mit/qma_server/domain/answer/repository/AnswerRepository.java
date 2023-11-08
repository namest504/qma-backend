package com.l1mit.qma_server.domain.answer.repository;

import com.l1mit.qma_server.domain.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerCustomRepository {

}
