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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    private String roomId;

    private Long memberId;

    private String nickName;

    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(
            final String message,
            final String roomId,
            final Long memberId,
            final String nickName) {
        this.message = message;
        this.roomId = roomId;
        this.memberId = memberId;
        this.nickName = nickName;
        this.createdAt = LocalDateTime.now();
    }
}
