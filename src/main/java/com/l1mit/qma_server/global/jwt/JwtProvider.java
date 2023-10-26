package com.l1mit.qma_server.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1mit.qma_server.domain.member.MemberService;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.global.auth.oauth.dto.PrincipalUser;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final MemberService memberService;
    private final Key key;
    private final ObjectMapper objectMapper;

    private final Long ACCESS_TOKEN_VALID_TIME = 1000 * 60L * 60L; // 60분
    private final Long REFRESH_TOKEN_VALID_TIME = 1000 * 60 * 60 * 24 * 7L; // 1주

    public JwtProvider(@Value("${jwt.secret}") String secretKey, MemberService memberService,
            ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.objectMapper = objectMapper;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtResponse createToken(Member member) {
        Claims claims = getClaims(member);
        String accessToken = getToken(member, claims, ACCESS_TOKEN_VALID_TIME);
        String refreshToken = getToken(member, claims, REFRESH_TOKEN_VALID_TIME);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Claims getClaims(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId().toString());
        return claims;
    }

    public String getToken(Member member, Claims claims, Long validationTime) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + validationTime))
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getTokenClaims(accessToken);
        Member member = memberService.findById(Long.parseLong(claims.get("id", String.class)));
        PrincipalUser principalUser = PrincipalUser.builder()
                .member(member)
                .build();

        return new UsernamePasswordAuthenticationToken(principalUser, "",
                principalUser.getAuthorities());
    }

    public Map<String, String> parseHeaders(String token) {
        try {
            String header = token.split("\\.")[0];
            return objectMapper.readValue(decodeHeader(header), Map.class);
        } catch (JsonProcessingException e) {
            throw new QmaApiException(ErrorCode.JSON_PROCESSING);
        }
    }

    public String decodeHeader(String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }

    private Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getTokenClaims(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
