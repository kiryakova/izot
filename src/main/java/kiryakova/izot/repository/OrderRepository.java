package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId"
            , nativeQuery = true)
    List<Order> findAllByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND is_finished = 'false' "
            , nativeQuery = true)
    Optional<Order> findUnfinishedOrderByUserId(@Param("userId") String userId);

}
