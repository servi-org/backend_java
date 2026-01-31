package live.servi.infrastructure.adapter.output.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import live.servi.domain.exception.DomainException;
import live.servi.domain.model.TokenParse;
import live.servi.domain.port.output.security.TokenGenerator;
import live.servi.infrastructure.exception.AppError;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Adaptador de salida para generacion de tokens JWT
 * Implementa el puerto TokenGenerator
 */
@Component
public class JwtToken implements TokenGenerator {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtToken(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration:3600000}") long expirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(UUID userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("email", email);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public TokenParse parseToken(String token) {
        try {
            Map<String, Object> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            String userId = (String) claims.get("userId");
            String email = (String) claims.get("email");
            return new TokenParse(userId, email);
        } catch (Exception e) {
            AppError error = AppError.of("JWT_PARSE_ERROR", "Error parsing JWT token", 401);
            throw new DomainException(error);
        }
    }
}
