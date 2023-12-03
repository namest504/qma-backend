package com.l1mit.qma_server.domain.chat.participant.domain;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private AuditEntity auditEntity;

    @Builder
    public ChatParticipant(final Member member, final ChatRoom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.auditEntity = new AuditEntity();
    }
}
