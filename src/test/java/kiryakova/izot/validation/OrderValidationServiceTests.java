package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderValidationServiceTests {
    private OrderValidationService orderValidationService;

    @Before
    public void init() {
        orderValidationService = new OrderValidationServiceImpl();
    }

    @Test
    public void isValidWithOrder_whenValid_true() {
        Order order = new Order();
        order.setUser(new User());
        boolean result = orderValidationService.isValid(order);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithOrder_whenNull_false() {
        Order order = null;
        boolean result = orderValidationService.isValid(order);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithOrder_whenInValidUser_false() {
        Order order = new Order();
        order.setUser(null);
        boolean result = orderValidationService.isValid(order);
        Assert.assertFalse(result);
    }

}
