package com.l1mit.qma_server.domain.answer.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.ANSWER_CONTENT_NOT_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.setting.validation.ValidationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AnswerRequestTest extends ValidationTest {

    @ParameterizedTest
    @CsvSource(value = {"답변 같은 내용:1", "3번 글에 답변:3"}, delimiter = ':')
    @DisplayName("대답에 들어온다면 처리한다.")
    void success(String content, Long questionId) {

        AnswerRequest answerRequest = AnswerRequest.builder()
                .content(content)
                .questionId(questionId)
                .build();

        assertThat(getConstraintViolations(answerRequest)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("대답이 Null 또는 Blank 라면 처리한다.")
    void blankContent(String content) {
        AnswerRequest answerRequest = AnswerRequest.builder()
                .content(content)
                .questionId(1L)
                .build();

        assertThat(getConstraintViolations(answerRequest).stream()
                .anyMatch(v -> v.getMessage().equals(ANSWER_CONTENT_NOT_BLANK)))
                .isTrue();

    }
}