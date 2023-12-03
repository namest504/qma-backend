package com.l1mit.qma_server.domain.chat.message.controller;

import com.l1mit.qma_server.domain.chat.message.service.ChatMessageService;
import com.l1mit.qma_server.domain.chat.message.dto.request.ChatMessageRequest;
import com.l1mit.qma_server.domain.chat.message.dto.response.ChatMessageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatMessageService chatMessageService;

    public ChatMessageController(final SimpMessageSendingOperations simpMessageSendingOperations, final ChatMessageService chatMessageService) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/message")
    public void message(@Valid ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        ChatMessageResponse chatMessageResponse = chatMessageService.saveChatMessage(Long.valueOf(String.valueOf(accessor.getSessionAttributes().get("memberId"))), request);
        simpMessageSendingOperations.convertAndSend("/pub/" + request.roomId(), chatMessageResponse);
    }
}
