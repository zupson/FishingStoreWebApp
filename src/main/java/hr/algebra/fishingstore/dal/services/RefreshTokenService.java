package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.repos.RefreshTokenRepository;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.RefreshToken;
import hr.algebra.fishingstore.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken create(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(t -> !t.isRevoked() && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public void revoke(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }

    public void revokeAll(User user) {
        refreshTokenRepository.findAllByUser(user).forEach(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }
}