package kiryakova.izot.service;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.validation.UserValidationService;
import kiryakova.izot.validation.UserValidationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTests {
    private User userDummy;
    private User userDummy2;
    private List<UserServiceModel> userServiceModels;

    @Mock
    private UserValidationService userValidation;

    @Mock
    private UserService userService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init() {

        this.userValidation = new UserValidationServiceImpl();

        this.userService = new UserServiceImpl(this.userRepository, this.userRoleService, this.userValidation, this.modelMapper, this.bCryptPasswordEncoder);


        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        this.userDummy = new User();
        userDummy.setId("668c3f56-7714-4014-b9eb-109a389f5c2f");
        userDummy.setUsername("aaaaa");
        userDummy.setEmail("testEmail@test.test");
        userDummy.setPassword(this.bCryptPasswordEncoder.encode("123"));

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetails() {
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        UserDetails userDetails = this.userService.loadUserByUsername(this.userDummy.getUsername());

        Assert.assertEquals("", this.userDummy.getUsername(), userDetails.getUsername());

        Mockito.verify(this.userRepository).findByUsername("aaaaa");
    }

    @Test
    public void registerUserCorrect() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(this.userDummy);
        Mockito.when(this.userRepository.save(this.userDummy)).thenReturn(this.userDummy);

        this.userService.registerUser(userServiceModel);

    }

    @Test
    public void loadUserByUsernameShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();

        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        UserDetails userDetails = this.userService.loadUserByUsername("aaaaa");

        Assert.assertEquals("", this.userDummy.getUsername(), userDetails.getUsername());

        Mockito.verify(this.userRepository).findByUsername("aaaaa");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldThrowException() {
        ModelMapper modelMapper = new ModelMapper();

        this.userService.findUserByUsername("TestName");

        Mockito.verify(this.userRepository).findByUsername("TestName");
    }

    @Test
    public void userService_checkIfUserNameAlreadyExists() {

        ModelMapper modelMapper = new ModelMapper();
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        boolean exists = userService.checkIfUsernameAlreadyExists(userDummy.getUsername());

        Assert.assertTrue(exists);
    }

    @Test
    public void userService_checkIfEmailAlreadyExists() {

        ModelMapper modelMapper = new ModelMapper();
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        boolean exists = userService.checkIfUsernameAlreadyExists(userDummy.getEmail());

        Assert.assertTrue(exists);
    }

    @Test
    public void userService_checkIfEmailAlreadyExistsForOtherUser() {

        ModelMapper modelMapper = new ModelMapper();

        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        this.userDummy2 = new User();
        userDummy2.setId("778c3f56-7714-4014-b9eb-109a389f5c2f");
        userDummy2.setUsername("bbbbb");
        userDummy2.setEmail("testEmail@test.test");
        userDummy2.setPassword(this.bCryptPasswordEncoder.encode("123"));

        boolean exists = userService.checkIfEmailExistsForOtherUser(userDummy2.getEmail(), userDummy2.getUsername());

        Assert.assertFalse(exists);
    }

    @Test(expected = Exception.class)
    public void userService_setUserAuthority() {

        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);
        userServiceModel.setId("668c3f56-7714-4014-b9eb-109a389f5c2f");

        this.userService.setUserAuthority(userDummy.getId(), "MODERATOR");

        Assert.assertFalse(userServiceModel.getAuthorities().isEmpty());
    }


    @Test(expected = Exception.class)
    public void userService_getUsers_whenTwoUsers() {

        ModelMapper modelMapper = new ModelMapper();

        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        this.userDummy2 = new User();
        userDummy2.setId("888c3f56-7714-4014-b9eb-109a389f5c2f");
        userDummy2.setUsername("bbbbb");
        userDummy2.setEmail("testEmail2@test.test");
        userDummy2.setPassword(this.bCryptPasswordEncoder.encode("123"));

        Mockito.when(this.userService.findAllUsers()).thenReturn(userServiceModels);

        List<UserServiceModel> usersFromDB = userService.findAllUsers();

        Assert.assertEquals(usersFromDB.size(), 2);
    }

    @Test
    public void editUserShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.userDummy));

        userServiceModel.setEmail(this.userDummy.getUsername());

        boolean result = this.userService.editUserProfile(userServiceModel);

        Assert.assertTrue("", result);
    }
}
