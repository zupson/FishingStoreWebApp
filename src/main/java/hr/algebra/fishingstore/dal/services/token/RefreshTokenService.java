package hr.algebra.fishingstore.dal.services.token;

import hr.algebra.fishingstore.dal.repos.RefreshTokenRepository;

import hr.algebra.fishingstore.model.entities.RefreshToken;
import hr.algebra.fishingstore.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String  REFRESH_EXPIRATION = "${jwt.refresh-expiration}";
    private final RefreshTokenRepository refreshTokenRepository;

    @Value(REFRESH_EXPIRATION)
    private Duration refreshExpiration;

    public RefreshToken create(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now(Clock.systemUTC()).plus(refreshExpiration));
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(t -> !t.isRevoked() &&
                        t.getExpiresAt().isAfter(LocalDateTime.now(Clock.systemUTC())))
                .orElse(false);
    }

    public void delete(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}