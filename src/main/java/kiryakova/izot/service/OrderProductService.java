package kiryakova.izot.service;

import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.domain.models.service.OrderProductServiceModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderProductService {
    OrderProductServiceModel findOrderProductByOrderIdAndProductId(String orderId, String productId) throws Exception;

    void addOrderProduct(OrderProduct orderProduct);
    List<OrderProductServiceModel> findOrderProductsByUser(String username) throws Exception;

    OrderProduct deleteOrderProduct(String id) throws Exception;

    List<OrderProductServiceModel> findOrderProductsByOrderId(String id);
}
