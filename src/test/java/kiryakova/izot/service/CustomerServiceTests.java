package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.entities.Order;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.repository.CustomerRepository;
import kiryakova.izot.repository.OrderRepository;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.validation.CustomerValidationService;
import kiryakova.izot.validation.CustomerValidationServiceImpl;
import kiryakova.izot.validation.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustomerServiceTests {
    @Autowired
    private CustomerRepository customerRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private UserService userService;
    @Mock
    private OrderService orderService;
    @Mock
    private UserValidationService userValidation;
    @Mock
    private CustomerValidationService customerValidation;
    @Mock
    private ModelMapper modelMapper;
    private Customer customer;
    private User user;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.customerValidation = new CustomerValidationServiceImpl();

        this.customerService = new CustomerServiceImpl(this.customerRepository, this.orderService, this.userService, this.userValidation, this.customerValidation, this.modelMapper);

        customer = new Customer();
        customer.setFirstName("Name1");
        customer.setLastName("Surname1");
        customer.setLocalDateTime(LocalDateTime.now());
        user = new User();
        user.setUsername("userName1");
        user.setEmail("stela@abv.bg");
        user.setPassword("aaa");
        customer.setUser(user);
        customer.setPhone("111");
        customer.setAddress("Address1");

        user = this.userRepository.saveAndFlush(user);
    }

    @Test(expected = Exception.class)
    public void customerService_findCustomerByUsername() throws Exception {

        customer = this.customerRepository.saveAndFlush(customer);

        CustomerServiceModel actual = customerService.findCustomerByUsername(customer.getUser().getUsername());
        CustomerServiceModel expected = this.modelMapper.map(customer, CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
    }

    @Test(expected = Exception.class)
    public void customerService_editCustomer() throws Exception {

        Order order = new Order();

        customer = this.customerRepository.save(customer);

        CustomerServiceModel toBeEdited = this.modelMapper.map(customer, CustomerServiceModel.class);

        customerService.editCustomer(toBeEdited.getUser().getUsername(), toBeEdited, order.getId());

        Customer actual = this.customerRepository.findCustomerByUserId(toBeEdited.getUser().getId()).orElse(null);
        Customer expected = this.customerRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
    }

    @Test(expected = Exception.class)
    public void customerService_findCustomerByOrderId() {

        customer = this.customerRepository.saveAndFlush(customer);
        Order order = new Order();
        order.setCustomer(customer);

        order = orderRepository.saveAndFlush(order);

        CustomerServiceModel actual = customerService.findCustomerByOrderId(order.getId());
        CustomerServiceModel expected = this.modelMapper.map(customer, CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
    }
}
