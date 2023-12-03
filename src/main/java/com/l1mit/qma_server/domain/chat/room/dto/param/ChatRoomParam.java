package com.l1mit.qma_server.domain.chat.room.dto.param;

import lombok.Builder;

public record ChatRoomParam(
        String roomTitle
) {

    @Builder
    public ChatRoomParam {
    }
}
