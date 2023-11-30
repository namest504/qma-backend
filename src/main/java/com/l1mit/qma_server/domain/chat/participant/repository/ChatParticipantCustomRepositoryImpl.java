package com.l1mit.qma_server.domain.chat.participant.repository;

import static com.l1mit.qma_server.domain.chat.participant.domain.QChatParticipant.chatParticipant;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;

public class ChatParticipantCustomRepositoryImpl implements ChatParticipantCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ChatParticipantCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Boolean existsByMemberIdAndRoomId(final Long memberId, final String roomId) {
        Integer result = jpaQueryFactory.selectOne()
                .from(chatParticipant)
                .where(chatParticipantChatRoomRoomIdEq(roomId),
                        chatParticipantMemberIdEq(memberId))
                .fetchFirst();
        return result != null;
    }

    @Override
    public Long deleteByMemberIdAndRoomId(final Long memberId, final String roomId) {
        long count = jpaQueryFactory.delete(chatParticipant)
                .where(chatParticipantChatRoomRoomIdEq(roomId),
                        chatParticipantMemberIdEq(memberId))
                .execute();
        return count;
    }

    private BooleanExpression chatParticipantMemberIdEq(final Long memberId) {
        return chatParticipant.member.id.eq(memberId);
    }

    private BooleanExpression chatParticipantChatRoomRoomIdEq(final String roomId) {
        return chatParticipant.chatRoom.roomId.eq(roomId);
    }
}
