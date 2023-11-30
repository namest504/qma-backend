package com.l1mit.qma_server.domain.session.service;

import com.l1mit.qma_server.domain.session.domain.Session;
import com.l1mit.qma_server.domain.session.repository.SessionRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void saveSession(String sessionId, Long memberId) {
        sessionRepository.save(Session.builder()
                .session(sessionId)
                .memberId(memberId)
                .build());
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public Long getMemberIdBySession(String sessionId) {
        Session findedSession = getSession(sessionId);
        return findedSession.getMemberId();
    }
}
