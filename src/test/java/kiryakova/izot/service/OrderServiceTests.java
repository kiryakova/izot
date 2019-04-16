package kiryakova.izot.service;

import kiryakova.izot.domain.entities.*;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.domain.models.service.OrderServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.OrderProductRepository;
import kiryakova.izot.repository.OrderRepository;
import kiryakova.izot.validation.OrderProductValidation;
import kiryakova.izot.validation.OrderValidationService;
import kiryakova.izot.validation.ProductValidationService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderServiceTests {
    @Autowired
    private OrderRepository orderRepository;
    private OrderProductRepository orderProductRepository;
    private OrderProductService orderProductService;
    private UserService userService;
    private ProductService productService;
    private UserValidationService userValidation;
    private ProductValidationService productValidation;
    private OrderValidationService orderValidation;
    private OrderService orderService;

    private ModelMapper modelMapper;
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
        user.setUsername("username1");
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

    @Test(expected = Exception.class)
    public void orderService_addOrder() {

        OrderServiceModel toBeSaved = new OrderServiceModel();
        toBeSaved.setFinalized(false);
        toBeSaved.setTotalPrice(new BigDecimal("0.01"));
        toBeSaved.setOrderDateTime(LocalDateTime.now());

        /*user = new User();
        user.setUsername("username1");
        user.setEmail("Email1");
        user.setPassword("aaa");*/
        toBeSaved.setUser(modelMapper.map(user, UserServiceModel.class));

        /*customer = new Customer();
        customer.setAddress("Address1");
        customer.setPhone("111");
        customer.setUser(user);
        customer.setLocalDateTime(LocalDateTime.now());
        customer.setFirstName("First1");
        customer.setFirstName("Last1");*/
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
    public void orderService_getAllOrders_whenTwoOrders() {

        orderRepository.deleteAll();
        orderRepository.save(order);
        Order order1 = new Order();
        order1.setFinished(false);
        order1.setTotalPrice(new BigDecimal("0.01"));
        order1.setOrderDateTime(LocalDateTime.now());

        user = new User();
        user.setUsername("username2");
        user.setEmail("Email2");
        user.setPassword("aaa2");
        //order1.setUser(user);

        customer = new Customer();
        customer.setAddress("Address2");
        customer.setPhone("1121");
        customer.setUser(user);
        customer.setLocalDateTime(LocalDateTime.now());
        customer.setFirstName("First2");
        customer.setFirstName("Last2");
        //order1.setCustomer(customer);

        orderRepository.save(order);
        orderRepository.save(order1);

        List<OrderServiceModel> ordersFromDB = orderService.findAllOrders();

        Assert.assertEquals(ordersFromDB.size(), 2);
    }

    @Test(expected = Exception.class)
    public void orderService_getAllOrdersByUsername() throws Exception {

        orderRepository.deleteAll();
        orderRepository.save(order);
        Order order1 = new Order();
        order1.setFinished(false);
        order1.setTotalPrice(new BigDecimal("0.01"));
        order1.setOrderDateTime(LocalDateTime.now());

        order1.setUser(user);
        order1.setCustomer(customer);

        orderRepository.save(order);
        orderRepository.save(order1);

        List<OrderServiceModel> ordersFromDB = orderService.findAllOrdersByUsername(order.getUser().getUsername());

        Assert.assertEquals(ordersFromDB.size(), 2);
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

/*        orderRepository.save(order);
        Order order1 = new Order();
        order1.setFinished(false);
        order1.setTotalPrice(new BigDecimal("0.01"));
        order1.setOrderDateTime(LocalDateTime.now());

        order1.setUser(user);
        order1.setCustomer(customer);
*/
        orderRepository.save(order);

        OrderServiceModel orderServiceModel = orderService.findUnfinishedOrderByUserName(order.getUser().getUsername());

        Assert.assertFalse(orderServiceModel.isFinalized());
    }

    @Test(expected = Exception.class)
    public void orderService_getOrderById() throws Exception {

        orderRepository.save(order);

        OrderServiceModel orderServiceModel = orderService.findOrderById(order.getId());

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
