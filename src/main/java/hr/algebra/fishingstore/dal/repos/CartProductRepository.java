package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.CartProduct;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<@NonNull CartProduct,@NonNull Long> {
}