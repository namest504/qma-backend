package com.l1mit.qma_server.domain.question.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.MBTI_MISMATCH;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.QUESTION_CONTENT_NOT_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.global.common.domain.MBTI;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.setting.validation.ValidationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class QuestionRequestTest extends ValidationTest {

    @ParameterizedTest
    @CsvSource(value = {"질문 내용:ENTJ"}, delimiter = ':')
    @DisplayName("정상적으로 처리한다.")
    void success(String content, String mbti) {

        QuestionRequest questionRequest = QuestionRequest.builder()
                .content(content)
                .mbti(mbti)
                .build();

        assertThat(getConstraintViolations(questionRequest)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("질문 내용이 비어있는 문자열이 들어오면 처리한다.")
    void fail_NullOrBlank(String content) {
        QuestionRequest questionRequest = QuestionRequest.builder()
                .content(content)
                .mbti("ENTP")
                .build();

        assertThat(getConstraintViolations(questionRequest).stream()
                .anyMatch(v -> v.getMessage().equals(QUESTION_CONTENT_NOT_BLANK)))
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"ANTP", "EQTP", "ENRP", "ENTU", "QSTP"})
    @DisplayName("MBTI가 올바르지 않은 문자열이 들어오면 처리한다.")
    void fail_NotMatchedMBTI(String mbti) {
        QuestionRequest questionRequest = QuestionRequest.builder()
                .content("content")
                .mbti(mbti)
                .build();

        assertThat(getConstraintViolations(questionRequest).stream()
                .allMatch(v -> v.getMessage().equals(MBTI_MISMATCH)))
                .isTrue();
    }

    private MbtiEntity getMbtiEntity(String mbti) {
        return MbtiEntity.builder()
                .mbti(MBTI.valueOf(mbti))
                .build();
    }
}