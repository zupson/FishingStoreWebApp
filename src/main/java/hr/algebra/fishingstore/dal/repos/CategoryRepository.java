package hr.algebra.fishingstore.dal.repos;


import hr.algebra.fishingstore.model.entities.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<@NonNull Category,@NonNull Long> {
}