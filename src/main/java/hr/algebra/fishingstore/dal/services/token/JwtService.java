package hr.algebra.fishingstore.dal.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import hr.algebra.fishingstore.model.entities.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String ROLE = "role";
    private static final String USER_ID = "userId";
    private static final String TYPE = "type";
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    
    public static final String JWT_EXPIRATION = "${jwt.expiration}";
    public static final String JWT_REFRESH_EXPIRATION = "${jwt.refresh-expiration}";
    public static final String JWT_SECRET = "${jwt.secret}";

    @Value(JWT_SECRET)
    private String secret;

    @Value(JWT_EXPIRATION)
    private Duration expiration;

    @Value(JWT_REFRESH_EXPIRATION)
    private Duration refreshExpiration;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim(ROLE, user.getRole().toString())
                .withClaim(USER_ID, user.getId())
                .withClaim(TYPE, ACCESS)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim(USER_ID, user.getId())
                .withClaim(TYPE, REFRESH)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration.toMillis()))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        DecodedJWT decoded = decodeToken(token);
        return decoded.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        return decodeToken(token).getClaim(USER_ID).asLong();
    }

    public String extractRole(String token) {
        return decodeToken(token).getClaim(ROLE).asString();
    }

    public boolean isAccessToken(String token) {
        return ACCESS.equals(decodeToken(token).getClaim(TYPE).asString());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}