package kiryakova.izot.service;

import kiryakova.izot.domain.entities.UserRole;
import kiryakova.izot.domain.models.service.UserRoleServiceModel;
import kiryakova.izot.repository.UserRoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRoleServiceTests {
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private ModelMapper modelMapper;
    private UserRole userRole;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.userRoleService = new UserRoleServiceImpl(this.userRoleRepository, this.modelMapper);

        userRole = new UserRole();
    }

    @Test
    public void userRoleService_getUserRoles_whenTwoRoles() {

        userRole.setAuthority("MODERATOR");
        userRole.setAuthority("USER");

        userRoleRepository.save(userRole);

        Set<UserRoleServiceModel> roles = userRoleService.findAllRoles();

        Assert.assertNotNull(roles);
    }

    @Test
    public void userRoleService_seedRoles() {

        userRoleService.seedUserRolesInDb();

        List<UserRole> roles= userRoleRepository.findAll();

        Assert.assertNotNull(roles);
    }

    @Test(expected = Exception.class)
    public void userRoleService_findByAuthority() {

        userRole.setAuthority("MODERATOR");

        userRoleRepository.saveAndFlush(userRole);

        UserRoleServiceModel userRoleServiceModel = userRoleService.findByAuthority("MODERATOR");

        Assert.assertNotNull(userRoleServiceModel);
    }
}
