package com.l1mit.qma_server;

import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QmaServerApplicationTests {

	@Test
	@DisplayName("서버의 시간대는 서울이다.")
	public void TimeZoneSetting() {
		QmaServerApplication app = new QmaServerApplication();
		app.started();

		Assertions.assertThat(TimeZone.getDefault())
				.isEqualTo(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
