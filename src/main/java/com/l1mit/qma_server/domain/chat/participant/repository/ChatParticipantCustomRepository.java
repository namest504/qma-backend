package com.l1mit.qma_server.domain.chat.participant.repository;


import java.util.Optional;

public interface ChatParticipantCustomRepository {

    Boolean existsByMemberIdAndRoomId(Long memberId, String roomId);

    Long deleteByMemberIdAndRoomId(Long memberId, String roomId);
}
