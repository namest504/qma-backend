package com.l1mit.qma_server.global.common.intercepter;

import static com.l1mit.qma_server.global.constants.MessageConstants.SESSION_AUTHENTICATION_ERROR;
import static com.l1mit.qma_server.global.constants.MessageConstants.SESSION_ROOM_ID_NOT_EXISTS;

import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.constants.MessageConstants;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.jwt.JwtValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompConversionException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class StompInterceptor implements ChannelInterceptor {

    private final JwtValidator jwtValidator;

    public StompInterceptor(final JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        Map<String, Object> sessionAttributes   = accessor.getSessionAttributes();

        switch (accessor.getCommand()) {
            case CONNECT -> {
                try{
                    String authorization = accessor.getFirstNativeHeader("Authorization");
                    SocialProvider socialProvider = SocialProvider.valueOf(accessor.getFirstNativeHeader("Provider"));
                    Long memberId = jwtValidator.extractMemberId(authorization, socialProvider);
                    sessionAttributes.put("memberId", memberId);
                } catch (Exception ex){
                    throw new MessageDeliveryException(SESSION_AUTHENTICATION_ERROR);
                }
            }
            case SUBSCRIBE -> {
                try {
                    String roomId = accessor.getFirstNativeHeader("roomId");
                    sessionAttributes.put("roomId", roomId);
                } catch (Exception ex) {
                    throw new MessageDeliveryException(SESSION_ROOM_ID_NOT_EXISTS);
                }

            }
        }
        return message;
    }

}
