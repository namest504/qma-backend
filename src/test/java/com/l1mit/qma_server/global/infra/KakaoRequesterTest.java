package com.l1mit.qma_server.global.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.OidcPublicKeyResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class KakaoRequesterTest {

    private MockWebServer mockWebServer;
    private KakaoRequester kakaoRequester;

    private final String CODE = "authcode";
    private final String REDIRECT_URI = "http://redirect-uri";

    private final String TOKEN_RESPONSE = "{\n"
            + "    \"token_type\": \"bearer\",\n"
            + "    \"access_token\": \"${ACCESS_TOKEN}\",\n"
            + "    \"id_token\": \"${ID_TOKEN}\",\n"
            + "    \"expires_in\": 7199,\n"
            + "    \"refresh_token\": \"${REFRESH_TOKEN}\",\n"
            + "    \"refresh_token_expires_in\": 86399,\n"
            + "    \"scope\": \"profile_image openid profile_nickname\"\n"
            + "}";

    private final String PUBLIC_KEY_RESPONSE = "{\n"
            + "    \"keys\": [\n"
            + "        {\n"
            + "            \"kid\": \"3f96980381e451efad0d2ddd30e3d3\",\n"
            + "            \"kty\": \"RSA\",\n"
            + "            \"alg\": \"RS256\",\n"
            + "            \"use\": \"sig\",\n"
            + "            \"n\": \"q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw\",\n"
            + "            \"e\": \"AQAB\"\n"
            + "        }, {\n"
            + "            \"kid\": \"9f252dadd5f233f93d2fa528d12fea\",\n"
            + "            \"kty\": \"RSA\",\n"
            + "            \"alg\": \"RS256\",\n"
            + "            \"use\": \"sig\",\n"
            + "            \"n\": \"qGWf6RVzV2pM8YqJ6by5exoixIlTvdXDfYj2v7E6xkoYmesAjp_1IYL7rzhpUYqIkWX0P4wOwAsg-Ud8PcMHggfwUNPOcqgSk1hAIHr63zSlG8xatQb17q9LrWny2HWkUVEU30PxxHsLcuzmfhbRx8kOrNfJEirIuqSyWF_OBHeEgBgYjydd_c8vPo7IiH-pijZn4ZouPsEg7wtdIX3-0ZcXXDbFkaDaqClfqmVCLNBhg3DKYDQOoyWXrpFKUXUFuk2FTCqWaQJ0GniO4p_ppkYIf4zhlwUYfXZEhm8cBo6H2EgukntDbTgnoha8kNunTPekxWTDhE5wGAt6YpT4Yw\",\n"
            + "            \"e\": \"AQAB\"\n"
            + "        }\n"
            + "    ]\n"
            + "}";

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.create(mockWebServer.url("").toString());
        kakaoRequester = new KakaoRequester(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    @DisplayName("Kakao의 id_token을 얻는데 성공한다.")
    void getOpenIdToken() {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(TOKEN_RESPONSE);
        mockWebServer.enqueue(response);

        IdTokenResponse idTokenResponse = kakaoRequester.getOpenIdToken(CODE, REDIRECT_URI);

        assertThat(idTokenResponse).isNotNull();
    }

    @Test
    @DisplayName("Kakao의 공개키 목록을 불러오는데 성공한다.")
    void getPublicKeys() {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(PUBLIC_KEY_RESPONSE);
        mockWebServer.enqueue(response);

        OidcPublicKeyResponse result = kakaoRequester.getPublicKeys();

        assertThat(result).isNotNull();
    }
}