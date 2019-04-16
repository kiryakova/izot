package kiryakova.izot.service;

import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.entities.UserRole;
import kiryakova.izot.domain.models.service.UserRoleServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRoleServiceTests {
    private UserRoleRepository userRoleRepository;
    private UserRoleService userRoleService;
    private ModelMapper modelMapper;
    private UserRole userRole;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.userRoleService = new UserRoleServiceImpl(this.userRoleRepository, this.modelMapper);

        userRole = new UserRole();
    }

    @Test(expected = Exception.class)
    public void userRoleService_seedRoles() {

        userRoleService.seedUserRolesInDb();

        List<UserRole> roles= userRoleRepository.findAll();

        Assert.assertEquals(roles.size(), 4);
    }

    @Test(expected = Exception.class)
    public void userRoleService_getUserRoles_whenTwoRoles() {

        userRole.setAuthority("MODERATOR");
        userRole.setAuthority("USER");

        userRoleRepository.save(userRole);

        Set<UserRoleServiceModel> roles = userRoleService.findAllRoles();

        Assert.assertEquals(roles.size(), 2);
    }

    @Test(expected = Exception.class)
    public void userRoleService_findByAuthority() {

        userRole.setAuthority("MODERATOR");

        userRoleRepository.saveAndFlush(userRole);

        UserRoleServiceModel userRoleServiceModel = userRoleService.findByAuthority("MODERATOR");

        Assert.assertNotNull(userRoleServiceModel);
    }
}
