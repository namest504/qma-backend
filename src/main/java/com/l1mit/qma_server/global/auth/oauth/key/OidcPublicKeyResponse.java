package com.l1mit.qma_server.global.auth.oauth.key;

import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import java.util.List;

public record OidcPublicKeyResponse(
        List<OidcPublicKey> keys
) {

    public OidcPublicKey getMatchedKey(final String kid, final String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(() -> new QmaApiException(ErrorCode.FAILED_OIDC_PUBLIC_KEY));
    }
}
