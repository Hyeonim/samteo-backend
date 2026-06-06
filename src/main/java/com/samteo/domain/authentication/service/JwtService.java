package com.samteo.domain.authentication.service;

import com.samteo.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * API 인증 과정에서 사용하는 JWT를 발급하고 검증하는 서비스이다.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    /**
     * 인증된 사용자 정보를 바탕으로 서명된 JWT를 생성한다.
     *
     * @param user 인증이 완료된 사용자
     * @return 서명된 JWT 문자열
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 유효한 JWT에서 애플리케이션 사용자 식별자를 추출한다.
     *
     * @param token JWT 문자열
     * @return 인증된 사용자 ID
     */
    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 토큰이 정상적으로 파싱 가능하며 아직 사용할 수 있는지 검증한다.
     *
     * @param token JWT 문자열
     * @return 토큰이 유효하면 {@code true}
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
