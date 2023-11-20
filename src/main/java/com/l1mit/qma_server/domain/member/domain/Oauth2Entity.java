package com.l1mit.qma_server.domain.member.domain;

import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
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
public class Oauth2Entity {

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider")
    private SocialProvider socialProvider;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Builder
    public Oauth2Entity(final SocialProvider socialProvider, final String accountId) {
        this.socialProvider = socialProvider;
        this.accountId = accountId;
    }
}
