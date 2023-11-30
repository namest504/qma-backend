package com.l1mit.qma_server.domain.chat.room.service;

import com.l1mit.qma_server.domain.chat.participant.service.ChatParticipantService;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.request.ChatRoomRequest;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.l1mit.qma_server.domain.chat.room.repository.ChatRoomRepository;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantService chatParticipantService;

    public ChatRoomService(final ChatRoomRepository chatRoomRepository, final ChatParticipantService chatParticipantService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantService = chatParticipantService;
    }

    public void createChatRoom(final ChatRoomRequest request) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomTitle(request.title())
                .build();
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getByRoomId(final String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));
    }

    public Page<ChatRoomResponse> searchChatRoom(final Pageable pageable, final ChatRoomParam param) {
        return chatRoomRepository.searchByCondition(pageable, param);
    }

    @Transactional
    public void storeSubscribeInfo(final Member member, final String roomId) {
        ChatRoom chatRoom = getByRoomId(roomId);

        if (!chatParticipantService.existsByMemberIdAndRoomId(member.getId(), roomId)) {
            chatParticipantService.save(chatRoom, member);
        }
    }
}
