package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.CustomerRepository;
import kiryakova.izot.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final UserValidationService userValidation;

    private final ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, OrderService orderService, UserService userService, UserValidationService userValidation, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.userService = userService;
        this.userValidation = userValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerServiceModel findCustomerByUsername(String username) throws Exception {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new Exception();
        }

        Customer customer = this.customerRepository.findCustomerByUserId(userServiceModel.getId()).orElse(null);
        CustomerServiceModel customerServiceModel = new CustomerServiceModel();

        if(customer != null){
            customerServiceModel = this.modelMapper.map(customer, CustomerServiceModel.class);
        }

        return customerServiceModel;
    }

    @Override
    public void editCustomer(String username, CustomerServiceModel customerServiceModel, String orderId) throws Exception {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(username);
        if(!userValidation.isValid(userServiceModel)) {
            throw new Exception();
        }

        Customer customer = this.customerRepository.findCustomerByUserId(userServiceModel.getId()).orElse(null);
        
        if ( customer == null || !customer.getFirstName().toLowerCase().equals(customerServiceModel.getFirstName().toLowerCase())
                || !customer.getLastName().toLowerCase().equals(customerServiceModel.getLastName().toLowerCase())
                || !customer.getAddress().toLowerCase().equals(customerServiceModel.getAddress().toLowerCase())
                || !customer.getPhone().toLowerCase().equals(customerServiceModel.getPhone().toLowerCase())) {

            customer = new Customer();
        }

        customer.setFirstName(customerServiceModel.getFirstName());
        customer.setLastName(customerServiceModel.getLastName());
        customer.setAddress(customerServiceModel.getAddress());
        customer.setPhone(customerServiceModel.getPhone());
        customer.setUser(this.modelMapper.map(userServiceModel, User.class));

        customer = this.customerRepository.saveAndFlush(customer);

        customerServiceModel = this.modelMapper.map(customer, CustomerServiceModel.class);

        this.orderService.setCustomerForOrder(orderId, customerServiceModel);
    }

    @Override
    public CustomerServiceModel findCustomerByOrderId(String orderId) {
        Customer customer = this.customerRepository.findCustomerByOrderId(orderId).orElse(null);
        if(customer != null) {
            return this.modelMapper.map(customer, CustomerServiceModel.class);
        }
        else {
            return null;
        }
    }
}
