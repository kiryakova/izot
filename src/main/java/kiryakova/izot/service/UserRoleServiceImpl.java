package kiryakova.izot.service;

import kiryakova.izot.domain.entities.UserRole;
import kiryakova.izot.domain.models.service.UserRoleServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedUserRolesInDb() {

        if(this.userRoleRepository.count() == 0) {
            this.userRoleRepository.saveAndFlush(new UserRole("USER"));
            this.userRoleRepository.saveAndFlush(new UserRole("MODERATOR"));
            this.userRoleRepository.saveAndFlush(new UserRole("ADMIN"));
            this.userRoleRepository.saveAndFlush(new UserRole("ROOT_ADMIN"));
        }
    }

    @Override
    public Set<UserRoleServiceModel> findAllRoles() {
        return this.userRoleRepository.findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, UserRoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public UserRoleServiceModel findByAuthority(String authority) {
        return this.modelMapper.map(this.userRoleRepository.findByAuthority(authority), UserRoleServiceModel.class);
    }

    /*@Override
    public void assignUserRoles(UserServiceModel userServiceModel, long numberOfUsers) {
        if(numberOfUsers == 0){
            userServiceModel
                    .setAuthorities(this.userRoleRepository
                            .findAll()
                            .stream()
                            .map(r -> this.modelMapper.map(r, UserRoleServiceModel.class))
                            .collect(Collectors.toSet()));
        }
    }
    */
}
