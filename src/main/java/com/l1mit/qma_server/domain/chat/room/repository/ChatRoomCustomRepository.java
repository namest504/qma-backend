package com.l1mit.qma_server.domain.chat.room.repository;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomCustomRepository {

    Optional<ChatRoom> findByRoomId(String roomId);

    Page<ChatRoomResponse> searchByCondition(Pageable pageable, ChatRoomParam param);
}
