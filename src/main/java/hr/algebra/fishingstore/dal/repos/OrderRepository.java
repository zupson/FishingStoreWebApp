package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}