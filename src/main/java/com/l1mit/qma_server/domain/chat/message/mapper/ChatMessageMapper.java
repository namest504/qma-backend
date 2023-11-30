package com.l1mit.qma_server.domain.chat.message.mapper;

import com.l1mit.qma_server.domain.chat.message.domain.ChatMessage;
import com.l1mit.qma_server.domain.chat.message.dto.response.ChatMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    public ChatMessageResponse entityToChatMessageResponse(final ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .userId(chatMessage.getMemberId())
                .nickname(chatMessage.getNickName())
                .messageId(chatMessage.getId())
                .messageContent(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
