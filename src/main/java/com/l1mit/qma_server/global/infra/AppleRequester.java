package com.l1mit.qma_server.global.infra;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.OidcPublicKeyResponse;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AppleRequester implements OauthAPIRequester {

    private final WebClient webclient;

    @Value("${oauth2.apple.api-key}")
    private String APPLE_OAUTH_API_KEY;

    @Value("${oauth2.apple.token-api-url}")
    private String APPLE_OAUTH_TOKEN_API_URI;

    @Value("${oauth2.apple.public-key-info}")
    private String APPLE_PUBLIC_KEY_INFO;

    public AppleRequester(WebClient webclient) {
        this.webclient = webclient;
    }

    @Override
    public IdTokenResponse getOpenIdToken(String code, String redirectUri) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", APPLE_OAUTH_API_KEY);
        data.add("redirect_uri", redirectUri);
        data.add("code", code);

        return webclient.post()
                .uri(APPLE_OAUTH_TOKEN_API_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(data)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new QmaApiException(ErrorCode.THIRD_PARTY_API_EXCEPTION)))
                .bodyToMono(IdTokenResponse.class)
                .block();
    }

    @Override
    @Cacheable(cacheNames = "apple")
    public OidcPublicKeyResponse getPublicKeys() {
        return webclient.get()
                .uri(APPLE_PUBLIC_KEY_INFO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new QmaApiException(ErrorCode.THIRD_PARTY_API_EXCEPTION)))
                .bodyToMono(OidcPublicKeyResponse.class)
                .block();
    }
}
