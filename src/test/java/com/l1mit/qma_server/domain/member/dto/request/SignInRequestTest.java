package com.l1mit.qma_server.domain.member.dto.request;

import static com.l1mit.qma_server.global.constants.RequestValidationConstants.CODE_REQUIRE;
import static com.l1mit.qma_server.global.constants.RequestValidationConstants.REDIRECT_URI_REQUIRE;
import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.setting.validation.ValidationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class SignInRequestTest extends ValidationTest {

    @ParameterizedTest
    @CsvSource(value = "{code:http://localhost:3000}", delimiter = ':')
    @DisplayName("로그인할 때 입력값이 정상이면 처리한다.")
    void SignInRequestValidation(String code, String redirectUri) {
        SignInRequest signInRequest = SignInRequest.builder()
                .code(code)
                .redirectUri(redirectUri)
                .build();

        assertThat(getConstraintViolations(signInRequest)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("로그인할 때 code가 공백이면 처리한다.")
    void SignInRequestValidationAboutCode(String code) {
        SignInRequest signInRequest = SignInRequest.builder()
                .code(code)
                .redirectUri("redirectUri")
                .build();

        assertThat(getConstraintViolations(signInRequest).stream()
                .anyMatch(v -> v.getMessage().equals(CODE_REQUIRE)))
                .isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("로그인할 때 redirect-uri가 공백이면 처리한다.")
    void SignInRequestValidationAboutRedirectUri(String redirectUri) {
        SignInRequest signInRequest = SignInRequest.builder()
                .code("code")
                .redirectUri(redirectUri)
                .build();

        assertThat(getConstraintViolations(signInRequest).stream()
                .anyMatch(v -> v.getMessage().equals(REDIRECT_URI_REQUIRE)))
                .isTrue();
    }
}