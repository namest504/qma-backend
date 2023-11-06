package com.l1mit.qma_server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import lombok.Builder;

public record MemberInfoResponse(
        @JsonProperty("provider")
        SocialProvider provider,
        @JsonProperty("mbti")
        MbtiEntity mbtiEntity
) {

    @Builder
    public MemberInfoResponse {
    }

    public MemberInfoResponse toMemberResponse(Member member) {
        return new MemberInfoResponse(member.getOauth2Entity().getSocialProvider(),
                member.getMbtiEntity());
    }
}
