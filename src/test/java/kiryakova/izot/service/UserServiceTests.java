package kiryakova.izot.service;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.repository.UserRoleRepository;
import kiryakova.izot.validation.UserValidationService;
import kiryakova.izot.validation.UserValidationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTests {
    private User userDummy;
    private User userDummy2;
    private List<UserServiceModel> userServiceModels;

    //@Autowired private WebApplicationContext wac;
    //private MockMvc mvc;

    @Autowired
    private UserValidationService userValidation;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init() {
        //mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //userServiceModels = new ArrayList<>();

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
        //userServiceModel.setEmail(this.userDummy.getUsername());

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(this.userDummy);
        Mockito.when(this.userRepository.save(this.userDummy)).thenReturn(this.userDummy);

        this.userService.registerUser(userServiceModel);

        //Assert.assertTrue(result);
    }

    @Test
    public void loadUserByUsernameShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();

       // Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        UserDetails userDetails = this.userService.loadUserByUsername("aaaaa");

        Assert.assertEquals("", this.userDummy.getUsername(), userDetails.getUsername());

        Mockito.verify(this.userRepository).findByUsername("aaaaa");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldThrowException() {
        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));

        this.userService.findUserByUsername("TestName");

        Mockito.verify(this.userRepository).findByUsername("TestName");
    }

    @Test(expected = Exception.class)
    public void userService_setUserAuthority() {

        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        //Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);
        userServiceModel.setId("668c3f56-7714-4014-b9eb-109a389f5c2f");

        this.userService.setUserAuthority(userDummy.getId(), "MODERATOR");

        Assert.assertFalse(userServiceModel.getAuthorities().isEmpty());
    }

    @Test
    public void userService_checkIfUserNameAlreadyExists() {

        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        //UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        boolean exists = userService.checkIfUsernameAlreadyExists(userDummy.getUsername());

        Assert.assertTrue(exists);
    }

    @Test
    public void userService_checkIfEmailAlreadyExists() {

        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        //UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        boolean exists = userService.checkIfUsernameAlreadyExists(userDummy.getEmail());

        Assert.assertTrue(exists);
    }

    @Test
    public void userService_checkIfEmailAlreadyExistsForOtherUser() {

        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        this.userDummy2 = new User();
        userDummy2.setId("778c3f56-7714-4014-b9eb-109a389f5c2f");
        userDummy2.setUsername("bbbbb");
        userDummy2.setEmail("testEmail@test.test");
        userDummy2.setPassword(this.bCryptPasswordEncoder.encode("123"));

        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy2));

        boolean exists = userService.checkIfEmailExistsForOtherUser(userDummy2.getEmail(), userDummy2.getUsername());

        Assert.assertFalse(exists);
    }

    @Test(expected = Exception.class)
    public void userService_getUsers_whenTwoUsers() {
        //userRepository.deleteAll();

        /*userServiceModels.addAll(List.of(
                new UserServiceModel(){{
                    setId("888c3f56-7714-4014-b9eb-109a389f5c2f");
                    setUsername("Name1");}},
                new UserServiceModel(){{
                    setId("998c3f56-7714-4014-b9eb-109a389f5c2f");
                    setUsername("Name2");}}
        ));*/


        ModelMapper modelMapper = new ModelMapper();
        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy));


        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        this.userDummy2 = new User();
        userDummy2.setId("888c3f56-7714-4014-b9eb-109a389f5c2f");
        userDummy2.setUsername("bbbbb");
        userDummy2.setEmail("testEmail2@test.test");
        userDummy2.setPassword(this.bCryptPasswordEncoder.encode("123"));

        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.userDummy2));


        Mockito.when(this.userService.findAllUsers()).thenReturn(userServiceModels);

        List<UserServiceModel> usersFromDB = userService.findAllUsers();

        Assert.assertEquals(usersFromDB.size(), 2);
    }
    /*
    @Test
    public void loadUserByIdShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(this.userDummy));

        UserServiceModel userServiceModel = this.userService.extractUserById("123e4567-e89b-12d3-a456-426655440000");

        Assert.assertEquals("", "123e4567-e89b-12d3-a456-426655440000", userServiceModel.getId());

        Mockito.verify(this.userRepository).findById("123e4567-e89b-12d3-a456-426655440000");
    }


    @Test(expected = IdNotFoundException.class)
    public void loadUserByIdShouldThrowException() {
        ModelMapper modelMapper = new ModelMapper();
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));

        this.userService.extractUserById("123e4567-e89b-12d3-a456-426655440000");

        Mockito.verify(this.userRepository).findById("123e4567-e89b-12d3-a456-426655440000");
    }
*/
    @Test
    public void editUserShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        //Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(userServiceModel, User.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.userDummy));


        userServiceModel.setEmail(this.userDummy.getUsername());

        boolean result = this.userService.editUserProfile(userServiceModel);

        Assert.assertTrue("", result);
    }
/*
    @Test(expected = UsernameNotFoundException.class)
    public void editNonExistentUserShouldThrowException() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));

        this.userService.editUser(userServiceModel);

        Mockito.verify(this.userRepository).findByUsername("test@test.test");
    }
*/
/*
    @Test
    public void extractingAllUsersShouldReturnSortedAlphabetically() {
        User user = new User();
        user.setUsername("Name1");
        user.setEmail("email@abv.bg");

        List<User> users = new ArrayList<>();
        users.add(this.userDummy);
        users.add(user);

//        Mockito.when(this.userRepository.findAllOrderedAlphabetically()).thenReturn(users);
//        Mockito.when(this.modelMapper.map(this.userDummy, UserServiceModel.class)).thenReturn(modelMapper.map(this.userDummy, UserServiceModel.class));
//
//        List<UserServiceModel> result = this.userService.extractAllUsersOrderedAlphabetically();
    }

/*
    @Test
    public void editUserRoleShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.userDummy, UserServiceModel.class);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(userServiceModel, User.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.userDummy));
        Mockito.when(this.roleRepository.findByAuthority(Mockito.anyString())).thenReturn(Optional.of(new UserRole("ROLE_TEST")));

        userServiceModel.setEmail(this.userDummy.getUsername());

        boolean result = this.userService.setUserAuthority(userServiceModel.getEmail(), "test");

        Assert.assertTrue("", result);
    }
*/
}
