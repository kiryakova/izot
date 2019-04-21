package kiryakova.izot.service;

import kiryakova.izot.domain.entities.*;
import kiryakova.izot.domain.models.service.*;
import kiryakova.izot.repository.OrderProductRepository;
import kiryakova.izot.validation.OrderProductValidationService;
import kiryakova.izot.validation.OrderProductValidationServiceImpl;
import kiryakova.izot.validation.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class OrderProductServiceTests {
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private OrderProductService orderProductService;
    @Mock
    private UserService userService;
    @Mock
    private UserValidationService userValidation;
    @Mock
    private OrderProductValidationService orderProductValidation;
    @Mock
    private ModelMapper modelMapper;
    private OrderProduct orderProduct;
    private Product product;
    private Order order;
    private List<OrderProduct> orderProducts;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.orderProductService = new OrderProductServiceImpl(this.orderProductRepository, this.userService, this.userValidation, this.orderProductValidation, this.modelMapper);
        this.orderProductValidation = new OrderProductValidationServiceImpl();

        orderProduct = new OrderProduct();
        orderProduct.setQuantity(1);
        orderProduct.setPrice(new BigDecimal("0.01"));
        product = new Product();
        product.setName("Product3");
        product.setDescription("Description1");
        product.setImageUrl("Image1.jpg");
        product.setPrice(new BigDecimal("0.01"));
        orderProduct.setProduct(product);
        order = new Order();
        order.setTotalPrice(new BigDecimal("0.01"));
        order.setFinished(false);
        orderProduct.setOrder(order);
        orderProducts = new ArrayList<>();
    }

    @Test
    public void orderProductService_findOrderProductByOrderId() {

        orderProductRepository.save(orderProduct);

        List<OrderProductServiceModel> orderProducts = orderProductService.findOrderProductsByOrderId(orderProduct.getId());

        Assert.assertNotNull(orderProducts);
    }

    @Test
    public void orderProductService_addOrderProduct() {

        orderProductService.addOrderProduct(orderProduct);

        OrderProduct expected = orderProductRepository.findById(orderProduct.getId()).orElse(null);

        Assert.assertNull(expected);
    }

    @Test
    public void orderProductService_findOrderProductByCategoryAndProducer() throws Exception {

        orderProductRepository.save(orderProduct);

        OrderProductServiceModel orderProductServiceModel = orderProductService.findOrderProductByOrderIdAndProductId(orderProduct.getOrder().getId(), orderProduct.getProduct().getId());

        Assert.assertNull(orderProductServiceModel);
    }

    @Test(expected = Exception.class)
    public void orderProductService_deleteOrderProduct() throws Exception {

        orderProductRepository.save(orderProduct);

        orderProductService.deleteOrderProduct(orderProduct.getId());

        long expectedCount = 0;
        long actualCount = orderProductRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

}
