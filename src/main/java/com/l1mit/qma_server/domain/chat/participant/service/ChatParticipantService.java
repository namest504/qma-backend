package com.l1mit.qma_server.domain.chat.participant.service;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.l1mit.qma_server.domain.chat.participant.repository.ChatParticipantRepository;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.stereotype.Service;

@Service
public class ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    public ChatParticipantService(final ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    public void save(final ChatRoom chatRoom, final Member member) {
        chatParticipantRepository.save(ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build());
    }

    public Boolean existsByMemberIdAndRoomId(final Long memberId, final String roomId) {
        return chatParticipantRepository.existsByMemberIdAndRoomId(memberId, roomId);
    }

    public void deleteByMemberIdAndRoomId(final Long memberId, final String roomId) {
        Long count = chatParticipantRepository.deleteByMemberIdAndRoomId(memberId, roomId);
        if (count == 0L) {
            throw new QmaApiException(ErrorCode.NOT_FOUND);
        }
    }
}
