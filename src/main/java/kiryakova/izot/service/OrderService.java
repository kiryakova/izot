package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.models.service.OrderServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    boolean addOrder(String productId, String name) throws Exception;

    List<OrderServiceModel> findAllOrders();

    List<OrderServiceModel> findAllOrdersByUserId(String userId);

    boolean confirmOrder(String id) throws Exception;

    BigDecimal deleteOrderProduct(String id) throws Exception;

    boolean setNewTotalPrice(BigDecimal totalPrice, Order order);

    OrderServiceModel findUnfinishedOrderByUserName(String username) throws Exception;
}
