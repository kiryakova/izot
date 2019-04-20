package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomerValidationServiceTests {
    private CustomerValidationService customerValidationService;

    @Before
    public void init() {
        customerValidationService = new CustomerValidationServiceImpl();
    }

    @Test
    public void isValidWithCustomer_whenValid_true() {
        Customer customer = new Customer();
        boolean result = customerValidationService.isValid(customer);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithCustomer_whenNull_false() {
        Customer customer = null;
        boolean result = customerValidationService.isValid(customer);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithCustomerServiceModel_whenValid_false() {
        CustomerServiceModel customerServiceModel = new CustomerServiceModel();
        boolean result = customerValidationService.isValid(customerServiceModel);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithCustomerServiceModel_whenNull_false() {
        CustomerServiceModel customerServiceModel = null;
        boolean result = customerValidationService.isValid(customerServiceModel);
        Assert.assertFalse(result);
    }
}
