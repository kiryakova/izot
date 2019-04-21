package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM categories ORDER BY category_name ASC"
            , nativeQuery = true)
    List<Category> findAllCategories();
}
