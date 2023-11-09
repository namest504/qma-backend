package com.l1mit.qma_server.domain.member.mapper;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.dto.response.MemberInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberInfoResponse entityToMemberInfoResponse(Member member) {
        return MemberInfoResponse.builder()
                .provider(member.getOauth2Entity().getSocialProvider())
                .mbtiEntity(member.getMbtiEntity())
                .build();
    }
}
