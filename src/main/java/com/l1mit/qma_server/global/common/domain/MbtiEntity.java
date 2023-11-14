package com.l1mit.qma_server.global.common.domain;

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
    @Column(name = "mbti")
    private MBTI mbti;

    @Builder
    public MbtiEntity(MBTI mbti) {
        this.mbti = mbti;
    }
}
