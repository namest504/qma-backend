package com.l1mit.qma_server.domain.question;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;

    @Embedded
    private MbtiEntity receiveMbtiEntity;

//    @Embedded
//    private MbtiEntity sendMbtiEntity;

    @Embedded
    private AuditEntity auditEntity;

    @Builder
    public Question(
            final Member member,
            final String content,
            final MbtiEntity receiveMbtiEntity) {
        this.member = member;
        this.content = content;
        this.receiveMbtiEntity = receiveMbtiEntity;
//        this.sendMbtiEntity = member.getMbtiEntity();
        this.auditEntity = new AuditEntity();
    }
}
