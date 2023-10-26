package com.l1mit.qma_server.domain.member.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignInRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private final String CODE = "authcode";
    private final String REDIRECT_URI = "http://redirect-uri";

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("로그인할 때 입력값이 정상이면 처리한다.")
    void SignInRequestValidation() {
        SignInRequest signInRequest = new SignInRequest(CODE, REDIRECT_URI);

        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(signInRequest);

        assertThat(validate).isEmpty();
    }

    @Test
    @DisplayName("로그인할 때 code가 공백이면 처리한다.")
    void SignInRequestValidationAboutCode() {
        SignInRequest signInRequest = new SignInRequest("", REDIRECT_URI);

        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(signInRequest);

        assertThat(validate).isNotEmpty()
                .anyMatch(v -> v.getMessage().equals("코드를 발급받고 진행해주세요."));
    }

    @Test
    @DisplayName("로그인할 때 redirect-uri가 공백이면 처리한다.")
    void SignInRequestValidationAboutRedirectUri() {
        SignInRequest signInRequest = new SignInRequest(CODE, "");

        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(signInRequest);

        assertThat(validate).isNotEmpty()
                .anyMatch(v -> v.getMessage().equals("redirect-uri를 설정해주세요."));
    }
}