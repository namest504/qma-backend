package com.l1mit.qma_server.global.auth.oauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.l1mit.qma_server.global.auth.oauth.key.PublicKeyGenerator;
import com.l1mit.qma_server.global.infra.KakaoRequester;
import com.l1mit.qma_server.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoAuthServiceTest {


    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PublicKeyGenerator publicKeyGenerator;

    @Mock
    private KakaoRequester kakaoRequester;


    @Test
    void getAccountId() {
        String idToken = "IdToken";
        Map<String, String> headers = new HashMap<>();
        PublicKey fakePublicKey = mock(PublicKey.class);

        Claims claims = Jwts.claims().setSubject("12345");

        given(jwtProvider.parseHeaders(idToken)).willReturn(headers);
        given(publicKeyGenerator.generatePublicKey(headers,
                kakaoRequester.getPublicKeys())).willReturn(fakePublicKey);
        given(jwtProvider.getTokenClaims(idToken, fakePublicKey)).willReturn(claims);

        // Act
        String accountId = kakaoAuthService.getAccountId(idToken);

        // Assert
        assertThat(accountId).isEqualTo("12345");
    }


}