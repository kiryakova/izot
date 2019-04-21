package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.UserRoleServiceModel;

import java.util.Set;

public interface UserRoleService {
    void seedUserRolesInDb();

    Set<UserRoleServiceModel> findAllRoles();

    UserRoleServiceModel findByAuthority(String authority);
}
