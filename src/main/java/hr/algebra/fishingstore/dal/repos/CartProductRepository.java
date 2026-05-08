package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Cart;
import hr.algebra.fishingstore.model.entities.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCart(Cart cart);
}