package com.l1mit.qma_server.global.auth.oauth.key;

import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import io.jsonwebtoken.JwtException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PublicKeyGenerator {

    public PublicKey generatePublicKey(final Map<String, String> tokenHeaders, OidcPublicKeyResponse oidcPublicKeyResponse) {
        OidcPublicKey publicKey = oidcPublicKeyResponse.getMatchedKey(tokenHeaders.get("kid"), tokenHeaders.get("alg"));

        return getPublicKey(publicKey);
    }

    private PublicKey getPublicKey(final OidcPublicKey publicKey) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
            byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes),
                    new BigInteger(1, eBytes));

            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());

            return keyFactory.generatePublic(publicKeySpec);

        } catch (JwtException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new QmaApiException(ErrorCode.FAILED_OIDC_PUBLIC_KEY);
        }
    }
}
