package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;

public interface UserValidationService {
    boolean isValid(User user);

    boolean isValid(UserServiceModel userServiceModel);
}
