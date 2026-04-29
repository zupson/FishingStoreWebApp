package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {
    @NotNull
    Optional<User> findByUsername(String username);
}