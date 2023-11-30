package com.l1mit.qma_server.domain.chat.participant.repository;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.l1mit.qma_server.domain.chat.room.repository.ChatRoomCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>, ChatParticipantCustomRepository {
}
