package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.LoginHistory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<@NonNull LoginHistory, @NonNull Long> {
}