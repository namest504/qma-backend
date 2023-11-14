package com.l1mit.qma_server.domain.answer.domain;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Embedded
    private AuditEntity auditEntity;

    @Builder
    public Answer(
            final String content,
            final Member member,
            final Question question) {
        this.content = content;
        this.member = member;
        this.question = question;
        this.auditEntity = new AuditEntity();
    }
}
