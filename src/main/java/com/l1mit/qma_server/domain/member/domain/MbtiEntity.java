package com.l1mit.qma_server.domain.member.domain;

import com.l1mit.qma_server.domain.member.domain.enums.mbti.Attitude;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Decision;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Lifestyle;
import com.l1mit.qma_server.domain.member.domain.enums.mbti.Perception;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "attitude")
    private Attitude attitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "perception")
    private Perception perception;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private Decision decision;

    @Enumerated(EnumType.STRING)
    @Column(name = "lifestyle")
    private Lifestyle lifestyle;

    @Builder
    public MbtiEntity(Attitude attitude, Perception perception, Decision decision,
            Lifestyle lifestyle) {
        this.attitude = attitude;
        this.perception = perception;
        this.decision = decision;
        this.lifestyle = lifestyle;
    }
}
