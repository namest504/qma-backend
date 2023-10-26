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

class GoogleRequesterTest {

    private MockWebServer mockWebServer;
    private GoogleRequester googleRequester;

    private final String CODE = "authcode";
    private final String REDIRECT_URI = "http://redirect-uri";
    private final String TOKEN_RESPONSE = "{\n" +
            "    \"access_token\": \"ACCESS_TOKEN_AT_HERE\",\n" +
            "    \"expires_in\": \"3599\",\n" +
            "    \"refresh_token\": \"REFRESH_TOKEN_AT_HERE\",\n" +
            "    \"scope\": \"https://www.googleapis.com/auth/drive.metadata.readonly https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/indexing openid https://www.googleapis.com/auth/userinfo.email\",\n"
            +
            "    \"token_type\": \"bearer\",\n" +
            "    \"id_token\": \"ID_TOKEN_AT_HERE\"\n" +
            "}";
    private final String PUBLIC_KEY_RESPONSE = "{\n"
            + "      \"e\": \"AQAB\",\n"
            + "      \"kty\": \"RSA\",\n"
            + "      \"use\": \"sig\",\n"
            + "      \"n\": \"keFudaSl4KpJ2xC-fIGOb4eD4hwmCVF3eWxginhvrcLNx3ygDjcN7wGRC-CkzJ12ymBGsTPnSBiTFTpwpa5LXEYi-wvN-RkwA8eptcFXIzCXn1k9TqFxaPfw5Qv8N2hj0ZnFR5KPMr1bgK8vktlBu_VbptXr9IKtUEpV0hQCMjmc0JAS61ZIgx9XhPWaRbuYUvmBVLN3ButKAoWqUuzdlP1arjC1R8bUWek3xKUuSSJmZ9oHIGU5omtTEgXRDiv442R3tle-gLcfcr57uPnaAh9bIgBJRZw2mjqP8uBZurq6YkuyUDFQb8NFkBxHigoEdE7di_OtEef2GFNLseE6mw\",\n"
            + "      \"kid\": \"7d334497506acb74cdeedaa66184d15547f83693\",\n"
            + "      \"alg\": \"RS256\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"alg\": \"RS256\",\n"
            + "      \"kty\": \"RSA\",\n"
            + "      \"use\": \"sig\",\n"
            + "      \"kid\": \"a06af0b68a2119d692cac4abf415ff3788136f65\",\n"
            + "      \"n\": \"yrIpMnHYrVPwlbC-IY8aU2Q6QKnLf_p1FQXNiTO9mWFdeYXP4cNF6QKWgy4jbVSrOs-4qLZbKwRvZhfTuuKW6fwj5lVZcNsq5dd6GXR65I8kwomMH-Zv_pDt9zLiiJCp5_GU6Klb8zMY_jEE1fZp88HIk2ci4GrmtPTbw8LHAkn0P54sQQqmCtzqAWp8qkZ-GGNITxMIdQMY225kX7Dx91ruCb26jPCvF5uOrHT-I6rFU9fZbIgn4T9PthruubbUCutKIR-JK8B7djf61f8ETuKomaHVbCcxA-Q7xD0DEJzeRMqiPrlb9nJszZjmp_VsChoQQg-wl0jFP-1Rygsx9w\",\n"
            + "      \"e\": \"AQAB\"\n"
            + "    }";

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.create(mockWebServer.url("").toString());
        googleRequester = new GoogleRequester(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    @DisplayName("Google의 id_token을 얻는데 성공한다.")
    void getOpenIdToken() {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(TOKEN_RESPONSE);
        mockWebServer.enqueue(response);

        IdTokenResponse idTokenResponse = googleRequester.getOpenIdToken(CODE, REDIRECT_URI);

        assertThat(idTokenResponse).isNotNull();
    }

    @Test
    @DisplayName("Google의의 공개키 목록을 불러오는데 성공한다.")
    void getPublicKeys() {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(PUBLIC_KEY_RESPONSE);
        mockWebServer.enqueue(response);

        OidcPublicKeyResponse result = googleRequester.getPublicKeys();

        assertThat(result).isNotNull();
    }
}