package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<@NonNull Product,@NonNull Long> {
}