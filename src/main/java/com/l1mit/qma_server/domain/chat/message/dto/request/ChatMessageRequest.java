package com.l1mit.qma_server.domain.chat.message.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.CHAT_MESSAGE_NOT_BLANK;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ROOM_ID_NOT_BLANK;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ChatMessageRequest(

        @NotBlank(message = ROOM_ID_NOT_BLANK)
        String roomId,

        @NotBlank(message = CHAT_MESSAGE_NOT_BLANK)
        String content
) {

    @Builder
    public ChatMessageRequest {
    }

}
