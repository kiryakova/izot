package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByName(String name);

    @Query(value = "SELECT * FROM products ORDER BY product_name ASC"
            , nativeQuery = true)
    List<Product> findAllProducts();

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId " +
            "ORDER BY product_name ASC"
            , nativeQuery = true)
    List<Product> findAllByCategoryId(@Param("categoryId") String categoryId);

    @Query(value = "SELECT * FROM products WHERE producer_id = :producerId " +
            "ORDER BY product_name ASC"
            , nativeQuery = true)
    List<Product> findAllByProducerId(@Param("producerId") String producerId);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId " +
            "AND producer_id = :producerId ORDER BY product_name ASC"
            , nativeQuery = true)
    List<Product> findAllByCategoryIdAndProducerId(@Param("categoryId") String categoryId,
                                                   @Param("producerId") String producerId);
}
