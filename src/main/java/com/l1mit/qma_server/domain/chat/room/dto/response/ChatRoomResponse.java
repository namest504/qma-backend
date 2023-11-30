package com.l1mit.qma_server.domain.chat.room.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

public record ChatRoomResponse(
        String roomId,
        String roomTitle,
//        Integer currentPeople,
//        Integer maxPeople,
        LocalDateTime createdAt

) {

    @Builder
    public ChatRoomResponse {
    }
}
