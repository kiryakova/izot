package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserValidationServiceTests {
    private UserValidationService userValidationService;

    @Before
    public void init() {
        userValidationService = new UserValidationServiceImpl();
    }

    @Test
    public void isValidWithProduct_whenValid_true() {
        User user = new User();
        boolean result = userValidationService.isValid(user);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithUser_whenNull_false() {
        User user = null;
        boolean result = userValidationService.isValid(user);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithUserServiceModel_whenValid_false() {
        UserServiceModel userServiceModel = new UserServiceModel();
        boolean result = userValidationService.isValid(userServiceModel);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithUserServiceModel_whenNull_false() {
        UserServiceModel userServiceModel = null;
        boolean result = userValidationService.isValid(userServiceModel);
        Assert.assertFalse(result);
    }
}
