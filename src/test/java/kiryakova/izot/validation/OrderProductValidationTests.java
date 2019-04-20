package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.OrderProduct;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderProductValidationTests {
    private OrderProductValidationService orderProductValidationService;

    @Before
    public void init() {
        orderProductValidationService = new OrderProductValidationServiceImpl();
    }

    @Test
    public void isValidWithOrderProduct_whenValid_true() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(new Order());
        boolean result = orderProductValidationService.isValid(orderProduct);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithOrderProduct_whenNull_false() {
        OrderProduct orderProduct = null;
        boolean result = orderProductValidationService.isValid(orderProduct);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithOrderProduct_whenInValidOrder_false() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(null);
        boolean result = orderProductValidationService.isValid(orderProduct);
        Assert.assertFalse(result);
    }
}
