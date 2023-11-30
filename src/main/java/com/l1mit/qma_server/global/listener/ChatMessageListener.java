package com.l1mit.qma_server.global.listener;

import com.l1mit.qma_server.domain.chat.room.service.ChatRoomService;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.domain.session.service.SessionService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class ChatMessageListener {

    private final ChatRoomService chatRoomService;
    private final SessionService sessionService;
    private final MemberService memberService;

    public ChatMessageListener(
            final ChatRoomService chatRoomService,
            final SessionService sessionService,
            final MemberService memberService) {
        this.chatRoomService = chatRoomService;
        this.sessionService = sessionService;
        this.memberService = memberService;
    }

    @EventListener(SessionConnectEvent.class)
    public void handleSessionConnect(SessionConnectEvent event) {
        sessionService.saveSession(
                getSimpSessionId(event.getMessage()),
                getMemberIdInSessionAttributes(event.getMessage()));
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        sessionService.deleteSession(getSimpSessionId(event.getMessage()));
    }

    @EventListener(SessionSubscribeEvent.class)
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        chatRoomService.storeSubscribeInfo(
                getMemberBySessionId(event.getMessage()),
                getRoomIdInSessionAttributes(event.getMessage()));
    }

    private String getSimpSessionId(Message<byte[]> event) {
        return String.valueOf(event.getHeaders().get("simpSessionId"));
    }

    private String getRoomIdInSessionAttributes(Message<byte[]> event) {
        Map<String, Object> attributes = (Map<String, Object>) event.getHeaders().get("simpSessionAttributes");
        return String.valueOf(attributes.get("roomId"));
    }

    private Long getMemberIdInSessionAttributes(Message<byte[]> event) {
        Map<String, Object> attributes = (Map<String, Object>) event.getHeaders().get("simpSessionAttributes");
        return Long.valueOf(String.valueOf(attributes.get("memberId")));
    }

    private Member getMemberBySessionId(Message<byte[]> event) {
        String sessionId = getSimpSessionId(event);
        Long memberId = sessionService.getMemberIdBySession(sessionId);
        return memberService.findById(memberId);
    }

}
