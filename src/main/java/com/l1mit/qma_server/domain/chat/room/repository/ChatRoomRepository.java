package com.l1mit.qma_server.domain.chat.room.repository;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {

}
