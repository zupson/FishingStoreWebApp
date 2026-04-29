package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.Address;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<@NonNull Address,@NonNull Long> {
}