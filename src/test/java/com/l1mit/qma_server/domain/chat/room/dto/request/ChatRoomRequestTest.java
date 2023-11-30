package com.l1mit.qma_server.domain.chat.room.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ROOM_TITLE_NOT_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.chat.room.dto.request.ChatRoomRequest;
import com.l1mit.qma_server.setting.validation.ValidationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChatRoomRequestTest extends ValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"방 제목", "방"})
    @DisplayName("정상적으로 처리한다.")
    void success(String title) {

        ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                .title(title)
                .build();

        assertThat(getConstraintViolations(chatRoomRequest)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("빈 문자열이 들어온다면 처리한다.")
    void fail(String title) {

        ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                .title(title)
                .build();

        assertThat(getConstraintViolations(chatRoomRequest).stream()
                .anyMatch(v -> v.getMessage().equals(ROOM_TITLE_NOT_BLANK)))
                .isTrue();
    }
}