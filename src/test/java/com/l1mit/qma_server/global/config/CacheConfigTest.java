package com.l1mit.qma_server.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.global.cache.CacheType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
public class CacheConfigTest {

    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("캐시 등록이 성공했는지 조회한다.")
    public void getAllCaches() {

        List<CacheType> cacheTypes = Arrays
                .stream(CacheType.values())
                .toList();

        List<String> cacheList = cacheManager.getCacheNames()
                .stream()
                .toList();

        assertThat(cacheList.size()).isEqualTo(cacheTypes.size());
    }
}
