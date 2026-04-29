package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Cart;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<@NonNull Cart,@NonNull Long > {
    Optional<Cart> findByUserId(Long userId);
}