package com.l1mit.qma_server.global.filter;

import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.jwt.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;

    public JwtAuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    private String ACCESS_TOKEN_HEADER = "Authorization";
    private String ACCESS_PROVIDER_HEADER = "Provider";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = Optional.ofNullable(getTokensFromHeader(request));
        Optional<String> provider = Optional.ofNullable(getProviderFromHeader(request));

        token.ifPresent(
                t -> {
                    provider.ifPresentOrElse(
                            p -> {
                                Authentication authentication = jwtValidator.getAuthentication(
                                        extractJwtFromBearerToken(t), SocialProvider.valueOf(p));
                                SecurityContextHolder.getContext()
                                        .setAuthentication(authentication);
                            },
                            () -> new QmaApiException(ErrorCode.UNAUTHORIZED)
                    );
                });
        filterChain.doFilter(request, response);
    }

    private String getTokensFromHeader(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN_HEADER);
    }

    private String extractJwtFromBearerToken(String token) {
        return token.substring(7);
    }

    private String getProviderFromHeader(HttpServletRequest request) {
        return request.getHeader(ACCESS_PROVIDER_HEADER);
    }
}
