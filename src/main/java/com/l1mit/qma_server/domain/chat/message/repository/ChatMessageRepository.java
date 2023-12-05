package com.l1mit.qma_server.domain.chat.message.repository;

import com.l1mit.qma_server.domain.chat.message.domain.ChatMessage;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesByRoomIdOrderByCreatedAtDesc(String roomId);
}
