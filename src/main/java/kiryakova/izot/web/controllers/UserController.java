package kiryakova.izot.web.controllers;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.models.binding.UserEditBindingModel;
import kiryakova.izot.domain.models.binding.UserRegisterBindingModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.domain.models.view.UserAllViewModel;
import kiryakova.izot.domain.models.view.UserProfileViewModel;
import kiryakova.izot.error.UserEditException;
import kiryakova.izot.error.UserRegisterException;
import kiryakova.izot.service.MailService;
import kiryakova.izot.service.UserService;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //private final RecaptchaService recaptchaService;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, ModelMapper modelMapper) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //this.recaptchaService = recaptchaService;
        this.mailService = mailService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login() {

        return this.view("login");

    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView) {

        modelAndView.addObject("user", new UserRegisterBindingModel());

        return this.view("register", modelAndView);

    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(ModelAndView modelAndView,
                                        @ModelAttribute(name = "user") @Valid UserRegisterBindingModel userRegisterBindingModel,
                                        //@RequestParam(name = "g-recaptcha-response") String gRecaptchaResponse,
                                        BindingResult bindingResult) {

        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "password", "Passwords don't match."));
        }

        if(this.userService.checkIfUsernameAlreadyExists(userRegisterBindingModel.getUsername())){
            bindingResult.addError(new FieldError("userRegisterBindingModel", "username",
                    String.format(ConstantsDefinition.UserConstants.USER_ALREADY_EXISTS, userRegisterBindingModel.getUsername())));
        }

        if(this.userService.checkIfEmailAlreadyExists(userRegisterBindingModel.getEmail())){
            bindingResult.addError(new FieldError("userRegisterBindingModel", "email",
                    String.format(ConstantsDefinition.UserConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())));
        }

        if(!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword()) ||
                //this.recaptchaService
                //        .verifyRecaptcha(request.getRemoteAddr()
                //                , gRecaptchaResponse) == null ||
                bindingResult.hasErrors()) {

            modelAndView.addObject("user", userRegisterBindingModel);

            return this.view("register", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper
                .map(userRegisterBindingModel, UserServiceModel.class);

        this.userService.registerUser(userServiceModel);

        this.mailService.sentRegistrationSuccessMessage(userRegisterBindingModel.getEmail(), userRegisterBindingModel.getUsername());

        return this.redirect("login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject("user",
                this.modelMapper
                        .map(this.userService
                                .findUserByUsername(principal.getName()), UserProfileViewModel.class));

        return this.view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject("user",
                this.modelMapper
                        .map(this.userService
                                .findUserByUsername(principal.getName()), UserEditBindingModel.class));

        return this.view("edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(ModelAndView modelAndView,
                                            @ModelAttribute(name = "user") @Valid UserEditBindingModel userEditBindingModel,
                                            BindingResult bindingResult){

        UserServiceModel userServiceModel = this.userService.findUserByUsername(userEditBindingModel.getUsername());

        if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getOldPassword(), userServiceModel.getPassword())) {
            bindingResult.addError(new FieldError("userEditBindingModel", "oldPassword", ConstantsDefinition.UserConstants.INCORRECT_PASSWORD));
        } else if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userEditBindingModel", "password", ConstantsDefinition.UserConstants.PASSWORDS_DO_NOT_MATCH));
        }

        if(!userEditBindingModel.getPassword()
                .equals(userEditBindingModel.getConfirmPassword()) ||
                bindingResult.hasErrors()) {

            modelAndView.addObject("user", userEditBindingModel);
            return this.view("edit-profile", modelAndView);
        }

        if (!this.userService.editUserProfile(this.modelMapper.map(userEditBindingModel, UserServiceModel.class))) {
            throw new UserEditException(String.format(ConstantsDefinition.UserConstants.UNSUCCESSFUL_USER_EDITING, userServiceModel.getEmail()));
        }


        //this.userService.editUserProfile(this.modelMapper.map(userEditBindingModel, UserServiceModel.class), userEditBindingModel.getOldPassword());
        return this.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UserAllViewModel> users = this.userService.findAllUsers()
                .stream()
                .map(u -> {
                    UserAllViewModel user = this.modelMapper.map(u, UserAllViewModel.class);
                    user.setAuthorities(u.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return this.view("all-users", modelAndView);
    }

    @PostMapping("/set-authority/{id}/{authority}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView setAuthority(@PathVariable("id") String id, @PathVariable("authority") String authority) {
        this.userService.setUserAuthority(id, authority);

        return this.redirect("/users/all");
    }
}