package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "SELECT * FROM customers WHERE user_id = :userId"
            , nativeQuery = true)
    Optional<Customer> findCustomerByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM customers c " +
            "JOIN orders o ON(o.customer_id = c.id) " +
            "WHERE o.id = :orderId " +
            "GROUP BY o.id"
            , nativeQuery = true)
    Optional<Customer> findCustomerByOrderId(@Param("orderId") String orderId);
}
