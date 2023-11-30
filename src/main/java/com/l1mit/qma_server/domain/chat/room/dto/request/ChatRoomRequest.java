package com.l1mit.qma_server.domain.chat.room.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ROOM_TITLE_NOT_BLANK;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ChatRoomRequest(
        @NotBlank(message = ROOM_TITLE_NOT_BLANK)
        String title
) {

    @Builder
    public ChatRoomRequest {
    }
}
