package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.User;
import hr.algebra.fishingstore.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByRole(Role role);
}