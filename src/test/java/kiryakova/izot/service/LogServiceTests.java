package kiryakova.izot.service;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.LogServiceModel;
import kiryakova.izot.repository.LogRepository;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class LogServiceTests {

    private List<LogServiceModel> logs;

    @MockBean
    JmsTemplate jmsTemplate;

    @Mock
    private LogRepository logRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LogService logService;

    @Mock
    private UserRepository userRepository;

    @Mock
    UserValidationService userValidation;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void init() {

        this.logService = new LogServiceImpl(this.logRepository, this.userService, this.userValidation, this.jmsTemplate, this.modelMapper);

        this.userValidation = new UserValidationServiceImpl();

    }

    @Test
    public void logService_createLog() {
        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("hashedPass");
        User user1 = new User();
        user1.setId("668c3f56-7714-4014-b9eb-109a389f7777");
        user1.setUsername("aaaaa");
        user1.setEmail("testEmail@test.test");
        user1.setPassword(this.bCryptPasswordEncoder.encode("123"));

        this.logService.logAction(user1.getUsername(), "event1");

    }

    @Test
    public void logService_addEvent() {
        logs = new ArrayList<>();
        logs = this.logService.findAllLogsByDateTimeDesc();
        Assert.assertNotNull(logs);
    }
}
