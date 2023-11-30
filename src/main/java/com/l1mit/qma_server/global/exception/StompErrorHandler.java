package com.l1mit.qma_server.global.exception;

import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        if (!ex.getMessage().isEmpty()) {
            return errorMessage(ex.getMessage());
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> errorMessage(String errorMessage) {

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders());
    }
}
