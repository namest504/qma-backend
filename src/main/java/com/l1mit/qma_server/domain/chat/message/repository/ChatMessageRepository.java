package com.l1mit.qma_server.domain.chat.message.repository;

import com.l1mit.qma_server.domain.chat.message.domain.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

}
