package com.l1mit.qma_server.domain.session.repository;

import com.l1mit.qma_server.domain.session.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, String> {

}
