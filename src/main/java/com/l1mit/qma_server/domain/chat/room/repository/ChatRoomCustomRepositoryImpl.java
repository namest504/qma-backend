package com.l1mit.qma_server.domain.chat.room.repository;

import static com.l1mit.qma_server.domain.chat.room.domain.QChatRoom.chatRoom;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ChatRoomCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<ChatRoom> findByRoomId(final String roomId) {
        ChatRoom result = jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.roomId.eq(roomId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<ChatRoomResponse> searchByCondition(final Pageable pageable, final ChatRoomParam chatRoomParam) {

        List<ChatRoomResponse> content = jpaQueryFactory.select(Projections.constructor(ChatRoomResponse.class,
                chatRoom.roomId.as("roomId"),
                chatRoom.roomTitle.as("roomTitle"),
                chatRoom.auditEntity.createdAt.as("createdAt")
                        ))
                .from(chatRoom)
                .where(roomTitleEq(chatRoomParam.roomTitle()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(chatRoom.count())
                .from(chatRoom)
                .where(roomTitleEq(chatRoomParam.roomTitle()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression roomTitleEq(final String roomTitle) {
        return roomTitle != null ? chatRoom.roomTitle.eq(roomTitle) : null;
    }

}
