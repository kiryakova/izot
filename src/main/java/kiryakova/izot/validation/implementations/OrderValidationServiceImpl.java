package kiryakova.izot.validation.implementations;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.validation.OrderValidationService;
import org.springframework.stereotype.Component;

@Component
public class OrderValidationServiceImpl implements OrderValidationService {
    @Override
    public boolean isValid(Order order) {
        return order != null;
    }
}
