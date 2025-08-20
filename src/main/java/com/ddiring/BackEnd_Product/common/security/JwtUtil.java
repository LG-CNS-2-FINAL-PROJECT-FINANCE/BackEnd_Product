package com.ddiring.BackEnd_Product.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secret;

    private SecretKey key() {
        // User 서비스가 Decoders.BASE64.decode(secretKey)로 발급하므로 동일하게 디코딩
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret.trim()));
    }

    public Claims parseClaims(String tokenOrBearer) {
        String token = tokenOrBearer == null ? "" : tokenOrBearer.replaceFirst("^Bearer\\s+", "").trim();
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
