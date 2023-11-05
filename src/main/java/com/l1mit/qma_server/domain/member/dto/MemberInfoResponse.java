package com.l1mit.qma_server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;

public record MemberInfoResponse(
        @JsonProperty("provider")
        SocialProvider provider,
        @JsonProperty("mbti")
        MbtiEntity mbtiEntity
) {

}
