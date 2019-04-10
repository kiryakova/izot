package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.OrderProduct;
import kiryakova.izot.validation.OrderProductValidation;
import org.springframework.stereotype.Component;

@Component
public class OrderProductValidationImpl implements OrderProductValidation {
    @Override
    public boolean isValid(OrderProduct orderProduct) {
        return orderProduct != null;
    }
}
