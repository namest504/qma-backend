package com.l1mit.qma_server.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.jwt.dto.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final ObjectMapper objectMapper;

    public JwtProvider(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> parseHeaders(final String token) {
        try {
            String header = token.split("\\.")[0];
            return objectMapper.readValue(decodeHeader(header), Map.class);
        } catch (JsonProcessingException e) {
            throw new QmaApiException(ErrorCode.JSON_PROCESSING);
        }
    }

    public String decodeHeader(final String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }

    public Claims getTokenClaims(final String token, final PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
