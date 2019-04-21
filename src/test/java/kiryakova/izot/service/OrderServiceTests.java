package kiryakova.izot.service;

import kiryakova.izot.domain.entities.*;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.CustomerRepository;
import kiryakova.izot.repository.OrderProductRepository;
import kiryakova.izot.repository.OrderRepository;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.validation.OrderValidationService;
import kiryakova.izot.validation.ProductValidationService;
import kiryakova.izot.validation.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class OrderServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private OrderProductService orderProductService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @Mock
    private UserValidationService userValidation;
    @Mock
    private ProductValidationService productValidation;
    @Mock
    private OrderValidationService orderValidation;
    @Mock
    private OrderService orderService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OrderRepository orderRepository;
    private Order order;
    private Customer customer;
    private User user;
    private Product product;

    private OrderProduct orderProduct;
    private List<Order> orders;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.orderService = new OrderServiceImpl(this.orderRepository, this.orderProductService, this.userService, this.productService, this.userValidation, this.productValidation, this.orderValidation, this.modelMapper);

        order = new Order();
        order.setFinished(false);
        order.setTotalPrice(new BigDecimal("0.01"));
        order.setOrderDateTime(LocalDateTime.now());

        user = new User();
        user.setUsername("aaaaa");
        user.setEmail("Email1");
        user.setPassword("aaa");
        order.setUser(user);

        customer = new Customer();
        customer.setAddress("Address1");
        customer.setPhone("111");
        customer.setUser(user);
        customer.setLocalDateTime(LocalDateTime.now());
        customer.setFirstName("First1");
        customer.setFirstName("Last1");
        order.setCustomer(customer);

        product = new Product();
        product.setName("Product1");
        product.setDescription("Description2");
        product.setImageUrl("Image2.jpg");
        product.setPrice(new BigDecimal("0.01"));

        orders = new ArrayList<>();
    }

    @Test
    public void orderService_getAllOrders_whenTwoOrders() {

        orderRepository.deleteAll();
        order.setOrderDateTime(LocalDateTime.now());
        order.setCustomer(customer);
        order.setUser(user);
        order.setTotalPrice(new BigDecimal("0.01"));
        order.setFinished(false);
        order.setId("307de219-99f2-4d9d-8e71-e626075e368c");
        orderRepository.save(order);

        List<OrderServiceModel> orders = orderService.findAllOrders();

        Assert.assertNotNull(orders);
    }

    @Test(expected = Exception.class)
    public void orderService_addOrder() {

        OrderServiceModel toBeSaved = new OrderServiceModel();
        toBeSaved.setFinalized(false);
        toBeSaved.setTotalPrice(new BigDecimal("0.01"));
        toBeSaved.setOrderDateTime(LocalDateTime.now());
        toBeSaved.setUser(modelMapper.map(user, UserServiceModel.class));

        toBeSaved.setCustomer(modelMapper.map(customer, CustomerServiceModel.class));

        orderService.addOrder(product.getId(), user.getUsername(), 1);

        Order actual = orderRepository.findById(toBeSaved.getId()).orElse(null);
        Order expected = orderRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
        Assert.assertEquals(expected.getUser(), actual.getUser());
        Assert.assertEquals(expected.getCustomer(), actual.getCustomer());
        Assert.assertEquals(expected.getOrderDateTime(), actual.getOrderDateTime());

    }

    @Test(expected = Exception.class)
    public void orderService_getAllOrdersByUsername() throws Exception {

        order.setOrderDateTime(LocalDateTime.now());

        order.setTotalPrice(new BigDecimal("0.01"));
        order.setFinished(false);
        order.setId("307de219-99f2-4d9d-8e71-e626075e368c");

        Mockito.when(this.customerRepository.save(this.customer)).thenReturn(this.customer);
        order.setCustomer(this.customer);

        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.user));
        order.setUser(this.user);
        Mockito.when(this.orderRepository.save(this.order)).thenReturn(this.order);

        List<OrderServiceModel> orders = orderService.findAllOrdersByUsername("aaaaa");

        Assert.assertNotNull(orders);
    }

    @Test(expected = Exception.class)
    public void orderService_confirmOrder() throws Exception {

        order = orderRepository.saveAndFlush(order);

        orderService.confirmOrder(order.getId());

        order = orderRepository.findById(order.getId()).orElse(null);

        Assert.assertTrue(order.isFinished());
    }

    @Test(expected = Exception.class)
    public void orderService_deleteOrderProduct() throws Exception {

        orderRepository.deleteAll();
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

        orderProduct = orderProductRepository.save(orderProduct);

        orderService.deleteOrderProduct(orderProduct.getId());

        long expectedCount = 0;
        long actualCount = orderProductRepository.count();

        Assert.assertEquals(expectedCount, actualCount);

    }

    @Test(expected = Exception.class)
    public void orderService_getUnfinishedOrderByUsername() throws Exception {

        orderRepository.save(order);

        OrderServiceModel orderServiceModel = orderService.findUnfinishedOrderByUserName(order.getUser().getUsername());

        Assert.assertFalse(orderServiceModel.isFinalized());
    }

    @Test(expected = Exception.class)
    public void orderService_getOrderById() throws Exception {
        order.setId("307de219-99f2-4d9d-8e71-e626075e368c");
        Mockito.when(orderRepository.saveAndFlush(this.order)).thenReturn(this.order);

        OrderServiceModel orderServiceModel = new OrderServiceModel();
        Mockito.when(orderService.findOrderById("307de219-99f2-4d9d-8e71-e626075e368c")).thenReturn(this.modelMapper.map(this.order, OrderServiceModel.class));

        Assert.assertFalse(orderServiceModel.isFinalized());
    }

    @Test(expected = Exception.class)
    public void orderService_getOrderById_WithInvalidId() throws Exception {

        OrderServiceModel orderServiceModel = orderService.findOrderById("Invalid");

        Assert.assertFalse(orderServiceModel.isFinalized());
    }

    @Test(expected = Exception.class)
    public void orderService_setCustomerForOrder() throws Exception {

        order = orderRepository.saveAndFlush(order);

        CustomerServiceModel customerServiceModel = modelMapper.map(customer, CustomerServiceModel.class);
        customerServiceModel.setFirstName("newCustomer");
        orderService.setCustomerForOrder(order.getId(), customerServiceModel);

        order = orderRepository.findById(order.getId()).orElse(null);

        Assert.assertEquals(order.getCustomer().getFirstName(), "newCustomer");
    }
}