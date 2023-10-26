package com.l1mit.qma_server.global.auth.oauth.key;

public record OidcPublicKey(
        String kid,
        String kty,
        String alg,
        String use,
        String n,
        String e
) {

}
