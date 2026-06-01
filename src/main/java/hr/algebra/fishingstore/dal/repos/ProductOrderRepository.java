package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {}