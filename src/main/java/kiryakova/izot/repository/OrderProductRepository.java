package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.domain.models.service.OrderProductServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {
    @Query(value = "SELECT * FROM order_products " +
            "WHERE order_id = :orderId AND product_id = :productId "
            , nativeQuery = true)
    Optional<OrderProduct> findOrderProductByOrderIdAndProductId(@Param("orderId") String orderId, @Param("productId") String productId);

    @Query(value = "SELECT * FROM order_products p " +
                    "JOIN orders o ON(p.order_id = o.id) " +
                    "JOIN users u ON(o.user_id = u.id) " +
                    "WHERE o.is_finished = 0 " +
                    "AND user_id = :userId " +
                    "GROUP BY p.id"
            , nativeQuery = true)
    List<OrderProduct> findOrderProductsByUser(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE op FROM order_products op " +
            "WHERE id = :orderProductId "
            , nativeQuery = true)
    void deleteOrderProductById(@Param("orderProductId") String id);


    @Query(value = "SELECT * FROM order_products " +
            "WHERE order_id = :orderId "
            , nativeQuery = true)
    List<OrderProduct> findOrderProductsByOrderId(@Param("orderId") String order_id);

}
