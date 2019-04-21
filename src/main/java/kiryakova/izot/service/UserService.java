package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

    boolean editUserProfile(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    void setUserAuthority(String id, String authority);

    boolean checkIfUsernameAlreadyExists(String username);

    boolean checkIfEmailAlreadyExists(String email);

    boolean checkIfEmailExistsForOtherUser(String email, String username);
}
