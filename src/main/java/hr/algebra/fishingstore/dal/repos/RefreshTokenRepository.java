package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.RefreshToken;
import hr.algebra.fishingstore.model.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}