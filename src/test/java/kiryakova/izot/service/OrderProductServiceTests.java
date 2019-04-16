package kiryakova.izot.service;

import kiryakova.izot.domain.entities.*;
import kiryakova.izot.domain.models.service.*;
import kiryakova.izot.repository.OrderProductRepository;
import kiryakova.izot.repository.OrderRepository;
import kiryakova.izot.repository.ProductRepository;
import kiryakova.izot.validation.OrderProductValidation;
import kiryakova.izot.validation.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderProductServiceTests {
    @Autowired
    private OrderProductRepository orderProductRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderProductService orderProductService;
    private UserService userService;
    private UserValidationService userValidation;
    private OrderProductValidation orderProductValidation;
    private ModelMapper modelMapper;
    private OrderProduct orderProduct;
    private Product product;
    private Order order;
    private List<OrderProduct> orderProducts;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.orderProductService = new OrderProductServiceImpl(this.orderProductRepository, this.userService, this.userValidation, this.orderProductValidation, this.modelMapper);

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

    @Test(expected = Exception.class)
    public void orderProductService_addOrderProduct() {

        OrderProductServiceModel toBeSaved = new OrderProductServiceModel();
        toBeSaved.setQuantity(1);
        toBeSaved.setPrice(new BigDecimal("0.01"));
        toBeSaved.setProduct(modelMapper.map(product, ProductServiceModel.class));
        toBeSaved.setOrder(modelMapper.map(order, OrderServiceModel.class));

        orderProductService.addOrderProduct(orderProduct);

        OrderProduct actual = orderProductRepository.findOrderProductByOrderIdAndProductId(orderProduct.getOrder().getId(), orderProduct.getProduct().getId()).orElse(null);
        OrderProduct expected = orderProductRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getQuantity(), actual.getQuantity());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getProduct(), actual.getProduct());
        Assert.assertEquals(expected.getOrder(), actual.getOrder());

    }

    @Test(expected = Exception.class)
    public void orderProductService_findOrderProductByCategoryAndProducer() throws Exception {

        orderProduct = orderProductRepository.saveAndFlush(orderProduct);

        OrderProductServiceModel orderProductServiceModel = orderProductService.findOrderProductByOrderIdAndProductId(orderProduct.getOrder().getId(), orderProduct.getProduct().getId());

        Assert.assertNotNull(orderProductServiceModel);
    }

    @Test(expected = Exception.class)
    public void orderProductService_deleteOrderProduct() throws Exception {

        orderProduct = orderProductRepository.save(orderProduct);

        orderProductService.deleteOrderProduct(orderProduct.getId());

        long expectedCount = 0;
        long actualCount = orderProductRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void orderProductService_findOrderProductByOrderId() {

        orderProduct = orderProductRepository.saveAndFlush(orderProduct);

        List<OrderProductServiceModel> orderProducts = orderProductService.findOrderProductsByOrderId(orderProduct.getId());

        Assert.assertNotNull(orderProducts);
    }
}
