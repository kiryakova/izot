package kiryakova.izot.validation;

import kiryakova.izot.domain.models.service.UserServiceModel;

public interface UserValidationService {
    boolean isValid(UserServiceModel user);
}
