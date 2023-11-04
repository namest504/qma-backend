package com.l1mit.qma_server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.l1mit.qma_server.domain.member.domain.MbtiEntity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;

public record MemberInfoResponse(
        @JsonProperty("provider")
        SocialProvider provider,
        @JsonProperty("mbti")
        MbtiEntity mbtiEntity
) {

}
