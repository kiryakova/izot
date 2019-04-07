package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.entities.UserRole;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    //private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleService userRoleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /*
    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        userServiceModel.setPassword(DigestUtils.sha256Hex(userServiceModel.getPassword()));

        return this.modelMapper.map(
                this.userRepository
                        .saveAndFlush(this.modelMapper.map(userServiceModel, User.class)),
                UserServiceModel.class);
    }

    @Override
    public UserServiceModel loginUser(UserServiceModel userServiceModel) {

        return this.userRepository.findByUsername(userServiceModel.getUsername())
                .filter(u -> u.getPassword().equals(DigestUtils.sha256Hex(userServiceModel.getPassword())))
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElse(null);
    }

    */
/*
    private void seedRolesInDb(){
        UserRole userRoleRootAdmin = new UserRole();
        userRoleRootAdmin.setAuthority("ROOT_ADMIN");
        this.userRoleRepository.saveAndFlush(userRoleRootAdmin);

        UserRole userRoleAdmin = new UserRole();
        userRoleAdmin.setAuthority("ADMIN");
        this.userRoleRepository.saveAndFlush(userRoleAdmin);

        UserRole userRoleModerator = new UserRole();
        userRoleModerator.setAuthority("MODERATOR");
        this.userRoleRepository.saveAndFlush(userRoleModerator);

        UserRole userRoleUser = new UserRole();
        userRoleUser.setAuthority("USER");
        this.userRoleRepository.saveAndFlush(userRoleUser);
    }
*/
/*
    private Set<UserRole> getAuthorities(String authority) {
        Set<UserRole> userAuthorities = new HashSet<>();

        userAuthorities.add(this.userRoleRepository.findByAuthority(authority));

        return userAuthorities;
    }
*/

/*
    private String getUserAuthority(String userId) {
        return this
                .userRepository
                .findById(userId)
                .get()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
    }
*/
    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        //Validation

        this.userRoleService.seedUserRolesInDb();

        if(this.userRepository.count() == 0){
            userServiceModel.setAuthorities(this.userRoleService.findAllRoles());
        } else{
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("USER"));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user = this.userRepository.saveAndFlush(user);

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsDefinition.UserConstants.NO_SUCH_USER));

    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsDefinition.UserConstants.NO_SUCH_USER));
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = this.userRepository
                .findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsDefinition.UserConstants.NO_SUCH_USER));

        if(!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new IllegalArgumentException(ConstantsDefinition.UserConstants.INCORRECT_PASSWORD);
        }

        user.setPassword("".equals(userServiceModel.getPassword()) ? user.getPassword() : this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        user.setEmail(userServiceModel.getEmail());

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll().stream().map(u -> this.modelMapper.map(u, UserServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void setUserAuthority(String id, String authority) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ConstantsDefinition.GlobalConstants.INCORRECT_ID));

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        switch (authority) {
            case "USER":
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("USER"));
                break;
            case "MODERATOR":
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("USER"));
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("MODERATOR"));
                break;
            case "ADMIN":
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("USER"));
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("MODERATOR"));
                userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("ADMIN"));
                break;
        }

        this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel, User.class));
    }
/*
    @Override
    public Set<UserServiceModel> getAll() {
        return this.userRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public UserServiceModel getById(String id) {
        User userEntity = this.userRepository
                .findById(id)
                .orElse(null);

        if(userEntity == null) return null;

        return this.modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getByUsername(String username) {
        User user = this.userRepository
                .findByUsername(username)
                .orElse(null);

        if(user == null) return null;

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public boolean promoteUser(String id) {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if(user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "USER":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("ADMIN"));
                break;
            default:
                throw new IllegalArgumentException(ConstantsDefinition.UserConstants.NO_ROLE_LOWER_THAN_USER);
        }

        this.userRepository.save(user);
        return true;
    }

    @Override
    public boolean demoteUser(String id) {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if(user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "ADMIN":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("USER"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, lower than USER");
        }

        this.userRepository.save(user);
        return true;
    }
    */

}