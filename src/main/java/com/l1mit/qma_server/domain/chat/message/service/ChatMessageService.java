package com.l1mit.qma_server.domain.chat.message.service;

import com.l1mit.qma_server.domain.chat.message.domain.ChatMessage;
import com.l1mit.qma_server.domain.chat.message.dto.request.ChatMessageRequest;
import com.l1mit.qma_server.domain.chat.message.dto.response.ChatMessageResponse;
import com.l1mit.qma_server.domain.chat.message.mapper.ChatMessageMapper;
import com.l1mit.qma_server.domain.chat.message.repository.ChatMessageRepository;
import com.l1mit.qma_server.domain.chat.room.service.ChatRoomService;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.domain.session.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {

    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageService(
            final MemberService memberService,
            final ChatMessageRepository chatMessageRepository,
            final ChatRoomService chatRoomService,
            final ChatMessageMapper chatMessageMapper) {
        this.memberService = memberService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Transactional
    public ChatMessageResponse saveChatMessage(final Long memberId, final ChatMessageRequest request) {
        ChatMessage savedChatMessage = chatMessageRepository.save(getChatMessage(memberId, request));
        return chatMessageMapper.entityToChatMessageResponse(savedChatMessage);
    }

    private ChatMessage getChatMessage(final Long memberId, final ChatMessageRequest request) {
        Member member = memberService.findById(memberId);
        ChatRoom chatRoom = chatRoomService.getByRoomId(request.roomId());
        return ChatMessage.builder()
                .message(request.content())
                .nickName(member.getNickname())
                .memberId(member.getId())
                .roomId(chatRoom.getRoomId())
                .build();
    }
}
