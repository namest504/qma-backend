package com.l1mit.qma_server.domain.chat.message.domain;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    private String id;

    @Indexed
    private String roomId;

    private String message;

    private Long memberId;

    private String nickName;

    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(
            final String message,
            final String roomId,
            final Long memberId,
            final String nickName) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.roomId = roomId;
        this.memberId = memberId;
        this.nickName = nickName;
        this.createdAt = LocalDateTime.now();
    }
}
