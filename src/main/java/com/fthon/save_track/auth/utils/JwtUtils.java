package com.fthon.save_track.auth.utils;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    private static final String EMAIL_CLAIM_NAME = "email";
    private static final String NICKNAME_CLAIM_NAME = "nickname";

    private final SecretKey key;
    private Long expireTime;

    public JwtUtils(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-token.expire}") Long expireTime
    ) {
        this.key =  Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.expireTime = expireTime;
    }


    public String sign(AuthenticatedUserDto userInfo, Date issueTime){
        Date expiryDate = new Date(issueTime.getTime() + expireTime);

        return Jwts.builder()
                .subject(userInfo.getUid())
                .claim(EMAIL_CLAIM_NAME, userInfo.getEmail())
                .claim(NICKNAME_CLAIM_NAME, userInfo.getNickname())
                .issuedAt(issueTime)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }


    /**
     * JWT 토큰이 올바른지 검증
     * @author minseok kim
     * @param authToken JWT Token
     * @throws RuntimeException 토큰이 올바르지 않은 경우
     */
    public void validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parse(authToken);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new RuntimeException("InvalidJWT Token", ex);
        }
    }


    /**
     * JWT로 부터 User 정보를 파싱
     * @author minseok kim
    */
    public AuthenticatedUserDto getUser(String token) {
        Claims payload = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();


        return new AuthenticatedUserDto(
                payload.getSubject(),
                payload.get(EMAIL_CLAIM_NAME, String.class),
                payload.get(NICKNAME_CLAIM_NAME, String.class)
        );
    }
}
