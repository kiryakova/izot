package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.error.UserEditException;
import kiryakova.izot.error.UserRegisterException;
import kiryakova.izot.repository.UserRepository;
import kiryakova.izot.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UserValidationService userValidation;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleService userRoleService,
                           UserValidationService userValidation,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.userValidation = userValidation;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {

        if(!userValidation.isValid(userServiceModel)){
            throw new IllegalArgumentException();
        }

        this.userRoleService.seedUserRolesInDb();

        if(this.userRepository.count() == 0){
            userServiceModel.setAuthorities(this.userRoleService.findAllRoles());
        } else{
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities()
                    .add(this.userRoleService.findByAuthority("USER"));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        try {
            this.userRepository.save(user);
        }catch (Exception ignored){
            throw new UserRegisterException(
                    String.format(
                            ConstantsDefinition.UserConstants.UNSUCCESSFUL_USER_REGISTRATION,
                            userServiceModel.getUsername())
            );
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);

        this.checkIfUserFound(user, username);

        return user;
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        User user = this.userRepository
                .findByUsername(username).orElse(null);

        this.checkIfUserFound(user, username);

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public boolean editUserProfile(UserServiceModel userServiceModel) {
        User user = this.userRepository
                .findByUsername(userServiceModel.getUsername()).orElse(null);

        this.checkIfUserFound(user, userServiceModel.getUsername());

        user.setPassword("".equals(userServiceModel.getPassword()) ? user.getPassword() : this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        user.setEmail(userServiceModel.getEmail());

        try {
            this.userRepository.save(user);
        }catch (Exception ignored){
            throw new UserEditException(
                    String.format(ConstantsDefinition.UserConstants.UNSUCCESSFUL_USER_EDITING,
                            userServiceModel.getUsername())
            );
        }

        return true;
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository
                .findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void setUserAuthority(String id, String authority) {
        User user = this.userRepository.findById(id).orElse(null);

        this.checkIfUserFound(user);

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        switch (authority) {
            case "USER":
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("USER"));
                break;
            case "MODERATOR":
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("USER"));
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("MODERATOR"));
                break;
            case "ADMIN":
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("USER"));
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("MODERATOR"));
                userServiceModel.getAuthorities()
                        .add(this.userRoleService.findByAuthority("ADMIN"));
                break;
        }

        try {
            this.userRepository.save(this.modelMapper.map(userServiceModel, User.class));
        }catch (Exception ignored){
            throw new UserEditException(
                    String.format(
                            ConstantsDefinition.UserConstants.UNSUCCESSFUL_USER_SET_AUTHORITY,
                            userServiceModel.getUsername())
            );
        }
    }

    @Override
    public boolean checkIfUsernameAlreadyExists(String username) {
        User user = this.userRepository
                .findByUsername(username).orElse(null);

        if(user == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIfEmailAlreadyExists(String email) {
        User user = this.userRepository
                .findByEmail(email).orElse(null);

        if(user == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIfEmailExistsForOtherUser(String email, String username) {
        User user = this.userRepository
                .findByEmail(email).orElse(null);

        if((user == null) || (user.getUsername().equals(username))) {
            return false;
        }

        return true;
    }

    private void checkIfUserFound(User user) {
        if(!userValidation.isValid(user)) {
            throw new UsernameNotFoundException(ConstantsDefinition
                    .UserConstants.NO_SUCH_USER);
        }
    }

    private void checkIfUserFound(User user, String username) {
        if(!userValidation.isValid(user)) {
            throw new UsernameNotFoundException(
                    String.format(
                            ConstantsDefinition.UserConstants.NO_USER_WITH_USERNAME,
                            username)
            );
        }
    }
}