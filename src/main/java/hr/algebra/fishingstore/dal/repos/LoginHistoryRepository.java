package hr.algebra.fishingstore.dal.repos;

import hr.algebra.fishingstore.model.entities.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findAllByOrderByLoginAtDesc();
}