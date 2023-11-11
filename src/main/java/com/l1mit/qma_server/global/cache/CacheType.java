package com.l1mit.qma_server.global.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    APPLE_PUBLIC_KEYS("apple", 12, 10000),
    GOOGLE_PUBLIC_KEYS("google", 12, 10000),
    KAKAO_PUBLIC_KEYS("kakao", 12, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
