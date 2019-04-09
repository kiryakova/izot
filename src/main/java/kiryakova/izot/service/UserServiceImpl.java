package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.User;
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
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleService userRoleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {

        this.userRoleService.seedUserRolesInDb();

        if(this.userRepository.count() == 0){
            userServiceModel.setAuthorities(this.userRoleService.findAllRoles());
        } else{
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.userRoleService.findByAuthority("USER"));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);

        return true;
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
    public boolean editUserProfile(UserServiceModel userServiceModel) {
        User user = this.userRepository
                .findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsDefinition.UserConstants.NO_SUCH_USER));


        user.setPassword("".equals(userServiceModel.getPassword()) ? user.getPassword() : this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        user.setEmail(userServiceModel.getEmail());

        this.userRepository.save(user);

        return true;
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
}