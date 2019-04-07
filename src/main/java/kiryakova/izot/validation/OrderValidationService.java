package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;

public interface OrderValidationService {
    boolean isValid(Order order);

    //boolean isValid(OrderServiceModel product);
}
