package kiryakova.izot.service;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.validation.UserValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTests {
    private UserRepository userRepository;
    private UserService userService;
    private UserRoleService userRoleService;
    private UserValidationService userValidation;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserServiceModel userServiceModel;
    private User user;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.userService = new UserServiceImpl(this.userRepository, this.userRoleService, this.userValidation, this.modelMapper, this.bCryptPasswordEncoder);

        userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("Username1");
        userServiceModel.setEmail("stelanz@abv.bg");
        userServiceModel.setPassword("aaaaaaaa");
    }

    @Test(expected = Exception.class)
    public void userService_addUser() {

        userService.registerUser(userServiceModel);

        User actual = userRepository.findByUsername("Username1").orElse(null);
        User expected = userRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());

    }

    @Test(expected = Exception.class)
    public void userService_loadUserByUsername() {

        userService.registerUser(userServiceModel);
        UserDetails user = userService.loadUserByUsername(userServiceModel.getUsername());

        UserDetails actual = modelMapper.map(userServiceModel, UserDetails.class);
        UserDetails expected = user;

        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());

    }

    @Test(expected = Exception.class)
    public void userService_findUserByUsername() {

        user = userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));

        UserServiceModel userServiceModelFounded = this.userService.findUserByUsername(userServiceModel.getUsername());

        Assert.assertNotNull(userServiceModelFounded);
    }

    @Test(expected = Exception.class)
    public void userService_editUserProfile() {

        user = userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));

        boolean result = this.userService.editUserProfile(userServiceModel);

        Assert.assertTrue(result);
    }

    @Test(expected = Exception.class)
    public void userService_getUsers_whenTwoUsers() {
        //userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("Username2");
        user1.setEmail("stelanz2@abv.bg");
        user1.setPassword("223333333");

        User user = modelMapper.map(userServiceModel, User.class);
        userRepository.save(user);
        userRepository.save(user1);

        List<UserServiceModel> productsFromDB = userService.findAllUsers();

        Assert.assertEquals(productsFromDB.size(), 2);
    }

    @Test(expected = Exception.class)
    public void userService_setUserAuthority() {

        user = userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));

        this.userService.setUserAuthority(user.getId(), "MODERATOR");

        Assert.assertFalse(user.getAuthorities().isEmpty());
    }

    @Test(expected = Exception.class)
    public void userService_checkIfUserNameAlreadyExists() {

        user = userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));

        boolean exists = userService.checkIfUsernameAlreadyExists(user.getUsername());

        Assert.assertTrue(exists);
    }

    @Test(expected = Exception.class)
    public void userService_checkIfEmailAlreadyExists() {

        user = userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));

        boolean exists = userService.checkIfEmailAlreadyExists(user.getUsername());

        Assert.assertTrue(exists);
    }

    @Test(expected = Exception.class)
    public void userService_checkIfEmailAlreadyExistsForOtherUser() {

        User user1 = new User();
        user1.setUsername("Username2");
        user1.setEmail("stelanz2@abv.bg");
        user1.setPassword("223333333");

        User user = modelMapper.map(userServiceModel, User.class);
        userRepository.save(user);
        userRepository.save(user1);

        boolean exists = userService.checkIfEmailExistsForOtherUser(user1.getEmail(), user1.getUsername());

        Assert.assertFalse(exists);
    }

}
