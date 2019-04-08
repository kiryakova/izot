package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CustomerServiceModel;

public interface CustomerService {
    CustomerServiceModel findCustomerByUsername(String username) throws Exception;

    void editCustomer(String username, CustomerServiceModel customerServiceModel, String orderId) throws Exception;

    CustomerServiceModel findCustomerByOrderId(String orderId);

}
