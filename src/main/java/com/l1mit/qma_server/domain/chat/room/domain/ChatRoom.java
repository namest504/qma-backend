package com.l1mit.qma_server.domain.chat.room.domain;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "room_id", nullable = false, unique = true)
    private String roomId;

    @Column(name = "room_title", nullable = false)
    private String roomTitle;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    private AuditEntity auditEntity;

    @Builder
    public ChatRoom(final String roomTitle) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.auditEntity = new AuditEntity();
        this.roomId = uuid;
        this.roomTitle = roomTitle;
    }
}
