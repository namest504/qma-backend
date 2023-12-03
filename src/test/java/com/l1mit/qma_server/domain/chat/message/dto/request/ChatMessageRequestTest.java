package com.l1mit.qma_server.domain.chat.message.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.CHAT_MESSAGE_NOT_BLANK;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ROOM_ID_NOT_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.setting.validation.ValidationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChatMessageRequestTest extends ValidationTest {

    @ParameterizedTest
    @CsvSource(value = {"roomId:메세지내용"}, delimiter = ':')
    @DisplayName("정상적으로 처리한다.")
    void success(String roomId, String content) {

        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .roomId(roomId)
                .content(content)
                .build();

        assertThat(getConstraintViolations(chatMessageRequest)).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {":메세지내용", " :메세지내용"}, delimiter = ':')
    @DisplayName("방번호 값이 비어있다면 처리한다.")
    void fail_emptyRoomId(String roomId, String content) {

        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .roomId(roomId)
                .content(content)
                .build();

        assertThat(getConstraintViolations(chatMessageRequest).stream()
                .anyMatch(v -> v.getMessage().equals(ROOM_ID_NOT_BLANK)))
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"roomId:", "roomId: "}, delimiter = ':')
    @DisplayName("메세지가 비어있다면 처리한다.")
    void fail_emptyContent(String roomId, String content) {

        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .roomId(roomId)
                .content(content)
                .build();

        assertThat(getConstraintViolations(chatMessageRequest).stream()
                .anyMatch(v -> v.getMessage().equals(CHAT_MESSAGE_NOT_BLANK)))
                .isTrue();
    }
}