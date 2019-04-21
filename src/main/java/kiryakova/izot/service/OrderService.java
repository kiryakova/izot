package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.domain.models.service.OrderServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    boolean addOrder(String productId, String name, int quantity);

    List<OrderServiceModel> findAllOrders();

    List<OrderServiceModel> findAllOrdersByUsername(String username) throws Exception;

    void confirmOrder(String id) throws Exception;

    void cancelOrder(String id) throws Exception;

    BigDecimal deleteOrderProduct(String id) throws Exception;

    OrderServiceModel findUnfinishedOrderByUserName(String username) throws Exception;

    OrderServiceModel findOrderById(String id);

    void setCustomerForOrder(String orderId, CustomerServiceModel customerServiceModel);
}
