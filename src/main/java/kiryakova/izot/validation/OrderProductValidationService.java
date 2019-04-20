package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.OrderProduct;

public interface OrderProductValidationService {
    boolean isValid(OrderProduct orderProduct);
}
