package com.l1mit.qma_server.global.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 에러가 발생하였습니다.");
        /*
        todo: ControllerAdvice 에서 처리할 수 있도록 로직 추가 필요
        throw new QmaApiException(ErrorCode.UNAUTHORIZED);
        */
    }
}
