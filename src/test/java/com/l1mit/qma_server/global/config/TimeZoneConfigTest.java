package com.l1mit.qma_server.global.config;

import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeZoneConfigTest {

    @Test
    @DisplayName("서버의 시간대는 서울이다.")
    public void TimeZoneSetting() {
        TimeZoneConfig config = new TimeZoneConfig();
        config.started();

        Assertions.assertThat(TimeZone.getDefault())
                .isEqualTo(TimeZone.getTimeZone("Asia/Seoul"));
    }
}