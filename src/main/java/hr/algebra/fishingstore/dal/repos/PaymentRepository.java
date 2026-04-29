package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Payment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<@NonNull Payment, @NonNull Long> {

}