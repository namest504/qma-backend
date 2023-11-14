package com.l1mit.qma_server.global.common.domain;

import lombok.Getter;

@Getter
public enum MBTI {
    ISTJ("ISTJ"),
    ISFJ("ISFJ"),
    INFJ("INFJ"),
    INTJ("INTJ"),
    ISTP("ISTP"),
    ISFP("ISFP"),
    INFP("INFP"),
    INTP("INTP"),
    ESTP("ESTP"),
    ESFP("ESFP"),
    ENFP("ENFP"),
    ENTP("ENTP"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),
    ENFJ("ENFJ"),
    ENTJ("ENTJ");

    private final String type;

    MBTI(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
