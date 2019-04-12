package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.models.service.CustomerServiceModel;

public interface CustomerValidationService {
    boolean isValid(Customer customer);

    boolean isValid(CustomerServiceModel customerServiceModel);
}
