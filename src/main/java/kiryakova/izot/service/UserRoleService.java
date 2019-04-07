package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.UserRoleServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;

import java.util.Set;

public interface UserRoleService {
    void seedUserRolesInDb();

    //void assignUserRoles(UserServiceModel userServiceModel, long numberOfUsers);

    Set<UserRoleServiceModel> findAllRoles();

    UserRoleServiceModel findByAuthority(String authority);
}
