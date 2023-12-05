package com.l1mit.qma_server.domain.chat.message.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

public record ChatMessageResponse(
        Long userId,
        String messageId,
        String nickname,
        String messageContent,
        LocalDateTime createdAt
) {

    @Builder
    public ChatMessageResponse {
    }
}
