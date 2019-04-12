package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Customer;
import kiryakova.izot.domain.models.service.CustomerServiceModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidationServiceImpl implements CustomerValidationService {
    @Override
    public boolean isValid(Customer customer) {
        return customer != null;
    }

    @Override
    public boolean isValid(CustomerServiceModel customerServiceModel) {
        return customerServiceModel != null;
    }
}
