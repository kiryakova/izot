package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.OrderProduct;

public interface OrderProductValidation {
    boolean isValid(OrderProduct orderProduct);
}
