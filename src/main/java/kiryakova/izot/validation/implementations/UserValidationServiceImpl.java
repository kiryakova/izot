package kiryakova.izot.validation.implementations;

import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.validation.UserValidationService;
import org.springframework.stereotype.Component;

@Component
public class UserValidationServiceImpl implements UserValidationService {
    @Override
    public boolean isValid(UserServiceModel user) {
        return user != null;
    }
}
