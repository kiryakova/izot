package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId " +
            "AND is_finished = 1 ORDER BY order_date_time DESC "
            , nativeQuery = true)
    List<Order> findAllOrdersByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId " +
            "AND is_finished = 0 "
            , nativeQuery = true)
    Optional<Order> findUnfinishedOrderByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM orders WHERE is_finished = 1 " +
            "ORDER BY order_date_time DESC "
            , nativeQuery = true)
    List<Order> findAllOrders();

    @Modifying
    @Transactional
    @Query(value = "DELETE o FROM orders o LEFT JOIN order_products op ON (op.order_id = o.id) WHERE o.is_finished = 0 "
            , nativeQuery = true)
    void deleteAllUnfinishedOrders();
}
