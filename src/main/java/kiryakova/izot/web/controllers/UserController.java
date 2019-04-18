package kiryakova.izot.web.controllers;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.models.binding.UserEditBindingModel;
import kiryakova.izot.domain.models.binding.UserRegisterBindingModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.domain.models.view.LogViewModel;
import kiryakova.izot.domain.models.view.UserViewModel;
import kiryakova.izot.domain.models.view.UserProfileViewModel;
import kiryakova.izot.error.UserEditException;
import kiryakova.izot.service.LogService;
import kiryakova.izot.service.MailService;
import kiryakova.izot.service.UserService;
import kiryakova.izot.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final LogService logService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, LogService logService, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, ModelMapper modelMapper) {
        this.userService = userService;
        this.logService = logService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    @PageTitle("Вход")
    public ModelAndView login() {

        return this.view("login");

    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Регистрация на потребител")
    public ModelAndView register(ModelAndView modelAndView) {

        modelAndView.addObject("user", new UserRegisterBindingModel());

        return this.view("register", modelAndView);

    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Регистрация на потребител")
    public ModelAndView registerConfirm(ModelAndView modelAndView,
                                        @ModelAttribute(name = "user") @Valid UserRegisterBindingModel userRegisterBindingModel,
                                        BindingResult bindingResult) {

        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "password", ConstantsDefinition.UserConstants.PASSWORDS_DO_NOT_MATCH));
        }

        if(this.userService.checkIfUsernameAlreadyExists(userRegisterBindingModel.getUsername())){
            bindingResult.addError(new FieldError("userRegisterBindingModel", "username",
                    String.format(ConstantsDefinition.UserConstants.USER_ALREADY_EXISTS, userRegisterBindingModel.getUsername())));
        }

        if(this.userService.checkIfEmailAlreadyExists(userRegisterBindingModel.getEmail())){
            bindingResult.addError(new FieldError("userRegisterBindingModel", "email",
                    String.format(ConstantsDefinition.UserConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())));
        }

        if(bindingResult.hasErrors()) {

            modelAndView.addObject("user", userRegisterBindingModel);

            return this.view("register", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper
                .map(userRegisterBindingModel, UserServiceModel.class);

        this.userService.registerUser(userServiceModel);

        this.logService.logAction(userServiceModel.getUsername(), String.format(ConstantsDefinition.UserConstants.USER_REGISTERED_SUCCESSFUL, userServiceModel.getUsername()));

        //this.mailService.sentRegistrationSuccessMessage(userRegisterBindingModel.getEmail(), userRegisterBindingModel.getUsername());

        return this.redirect("login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Потребителски профил")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject("user",
                this.modelMapper
                        .map(this.userService
                                .findUserByUsername(principal.getName()), UserProfileViewModel.class));

        return this.view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Редакция на потребителския профил")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject("user",
                this.modelMapper
                        .map(this.userService
                                .findUserByUsername(principal.getName()), UserEditBindingModel.class));

        return this.view("edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Редакция на потребителския профил")
    public ModelAndView editProfileConfirm(Principal principal,
                                           ModelAndView modelAndView,
                                           @ModelAttribute(name = "user") @Valid UserEditBindingModel userEditBindingModel,
                                           BindingResult bindingResult){

        UserServiceModel userServiceModel = this.userService.findUserByUsername(userEditBindingModel.getUsername());

        if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getOldPassword(), userServiceModel.getPassword())) {
            bindingResult.addError(new FieldError("userEditBindingModel", "oldPassword", ConstantsDefinition.UserConstants.INCORRECT_PASSWORD));
        }

        if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userEditBindingModel", "password", ConstantsDefinition.UserConstants.PASSWORDS_DO_NOT_MATCH));
        }

        if(this.userService.checkIfEmailExistsForOtherUser(userEditBindingModel.getEmail(), userEditBindingModel.getUsername())){
            bindingResult.addError(new FieldError("userEditBindingModel", "email",
                    String.format(ConstantsDefinition.UserConstants.EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail())));
        }

        if(bindingResult.hasErrors()) {

            modelAndView.addObject("user", userEditBindingModel);
            return this.view("edit-profile", modelAndView);
        }

        userServiceModel = this.modelMapper.map(userEditBindingModel, UserServiceModel.class);

        if (!this.userService.editUserProfile(userServiceModel)) {
            throw new UserEditException(String.format(ConstantsDefinition.UserConstants.UNSUCCESSFUL_USER_EDITING, userServiceModel.getEmail()));
        }

        this.logService.logAction(principal.getName(), String.format(ConstantsDefinition.UserConstants.USER_PROFILE_EDITED_SUCCESSFUL, userServiceModel.getUsername()));

        return this.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("Всички потребители")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UserViewModel> users = this.userService.findAllUsers()
                .stream()
                .map(u -> {
                    UserViewModel user = this.modelMapper.map(u, UserViewModel.class);
                    user.setAuthorities(u.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return this.view("all-users", modelAndView);
    }

    @PostMapping("/set-authority/{id}/{authority}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("Всички потребители")
    public ModelAndView setAuthority(@PathVariable("id") String id, @PathVariable("authority") String authority) {
        this.userService.setUserAuthority(id, authority);

        return this.redirect("/users/all");
    }

    @GetMapping("/logs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView logs(ModelAndView modelAndView) {

        List<LogViewModel> logs = this.logService.findAllLogsByDateTimeDesc()
                .stream()
                .map(l -> this.modelMapper.map(l, LogViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("logs", logs);

        return this.view("logs", modelAndView);

    }
}