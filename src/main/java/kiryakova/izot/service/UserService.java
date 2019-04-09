package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {

    boolean registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

    boolean editUserProfile(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    void setUserAuthority(String id, String authority);

    //UserServiceModel loginUser(UserServiceModel userServiceModel);

    //boolean createUser(UserServiceModel userServiceModel);

    //Set<UserServiceModel> getAll();

    //UserServiceModel getById(String id);

    //UserServiceModel getByUsername(String username);

    //boolean promoteUser(String id);

    //boolean demoteUser(String id);
}
