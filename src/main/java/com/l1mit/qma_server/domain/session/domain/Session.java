package com.l1mit.qma_server.domain.session.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    private String session;

    private Long memberId;

    @Builder
    public Session(String session, Long memberId) {
        this.session = session;
        this.memberId = memberId;
    }
}
