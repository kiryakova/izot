package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class OrderValidationServiceImpl implements OrderValidationService {
    @Override
    public boolean isValid(Order order) {
        return order != null
                && isUserValid(order.getUser());
    }

    private boolean isUserValid(User user) {
        return user != null;
    }
}
