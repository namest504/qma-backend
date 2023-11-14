package com.l1mit.qma_server.domain.question.domain;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.common.domain.AuditEntity;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @Embedded
    private MbtiEntity receiveMbtiEntity;

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
        this.auditEntity = new AuditEntity();
    }
}
