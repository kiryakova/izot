package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.binding.UserEditBindingModel;
import kiryakova.izot.domain.models.binding.UserLoginBindingModel;
import kiryakova.izot.domain.models.binding.UserRegisterBindingModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.domain.models.view.UserAllViewModel;
import kiryakova.izot.domain.models.view.UserProfileViewModel;
import kiryakova.izot.service.MailService;
import kiryakova.izot.service.RecaptchaService;
import kiryakova.izot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    //private final RecaptchaService recaptchaService;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, MailService mailService, ModelMapper modelMapper) {
        this.userService = userService;
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

        if(!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword()) ||
                //this.recaptchaService
                //        .verifyRecaptcha(request.getRemoteAddr()
                //                , gRecaptchaResponse) == null ||
                bindingResult.hasErrors()) {

            modelAndView.addObject("user", userRegisterBindingModel);

            return this.view("register", modelAndView);
        }

        UserServiceModel userServiceModel = this.userService
                .registerUser(this.modelMapper
                        .map(userRegisterBindingModel, UserServiceModel.class));

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

        if(!userEditBindingModel.getPassword()
                .equals(userEditBindingModel.getConfirmPassword()) ||
                bindingResult.hasErrors()) {

            modelAndView.addObject("user", userEditBindingModel);
            return this.view("edit-profile", modelAndView);
        }

        this.userService.editUserProfile(this.modelMapper.map(userEditBindingModel, UserServiceModel.class), userEditBindingModel.getOldPassword());
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

        return super.view("all-users", modelAndView);
    }

    @PostMapping("/set-authority/{id}/{authority}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView setAuthority(@PathVariable("id") String id, @PathVariable("authority") String authority) {
        this.userService.setUserAuthority(id, authority);

        return super.redirect("/users/all");
    }

    /*
    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserAuthority(id, "USER");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        this.userService.setUserAuthority(id, "MODERATOR");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserAuthority(id, "ADMIN");

        return super.redirect("/users/all");
    }
    */
    /*
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView){

        if(modelAndView.isEmpty()){
            modelAndView.addObject("model", new UserRegisterBindingModel());
        }

        //return this.view("register");

        //modelAndView.setViewName("/register");
        //return modelAndView;

        return super.view("register", modelAndView);

    }

    @PostMapping("/register")
    //public ModelAndView registerConfirm(@ModelAttribute @Valid UserRegisterBindingModel bindingModel, ModelAndView modelAndView, BindingResult bindingResult, RedirectAttributes redirectAttributes){
    public ModelAndView registerConfirm(@ModelAttribute(name = "model") @Valid UserRegisterBindingModel bindingModel, ModelAndView modelAndView,
                                        BindingResult bindingResult){
        if(!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())){
            //redirectAttributes.addFlashAttribute("model", bindingModel);

            //redirectAttributes.addFlashAttribute("errors", new ArrayList<String>(Arrays.asList("Passwords don't much!")));

            modelAndView.addObject("model", bindingModel);

            modelAndView.addObject("errors", new ArrayList<String>(Arrays.asList("Passwords don't much!")));

            //return this.redirect("/register");
            return super.redirect("/register");
        }

        if(bindingResult.hasErrors()){
            //redirectAttributes.addFlashAttribute("model", bindingModel);

            //redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()));

            modelAndView.addObject("model", bindingModel);

            modelAndView.addObject("errors", bindingResult.getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()));


            //return this.redirect("/register");

            //modelAndView.setViewName("/register");
            //return modelAndView;

            return super.view("register", modelAndView);
        }


        this.userService.registerUser(this.modelMapper.map(bindingModel, UserServiceModel.class));

        //return this.redirect("/login");

        //modelAndView.setViewName("redirect:/login");
        //return modelAndView;

        return super.redirect("/login");

    }

	@GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView){
        if(modelAndView.isEmpty()){
            modelAndView.addObject("model", new UserLoginBindingModel());
            //model.addAttribute("model", new UserLoginBindingModel());
        }

        //return this.view("login");

        //modelAndView.setViewName("/login");
        //return modelAndView;

        return super.view("login", modelAndView);
    }

    @PostMapping("/login")
    //public ModelAndView loginConfirm(@ModelAttribute UserLoginBindingModel bindingModel, ModelAndView modelAndView, RedirectAttributes redirectAttributes, HttpSession session){
    public ModelAndView loginConfirm(@ModelAttribute(name = "model") UserLoginBindingModel bindingModel, ModelAndView modelAndView,
                                     HttpSession session){

        UserServiceModel userServiceModel = this.userService.loginUser(this.modelMapper.map(bindingModel, UserServiceModel.class));

        if(userServiceModel == null){
            modelAndView.addObject("model", bindingModel);

            modelAndView.addObject("errors", new ArrayList<String>(Arrays.asList("Login user failed!")));

            //return this.view("/login");

            //modelAndView.setViewName("/login");
            //return modelAndView;

            return super.view("login", modelAndView);
        }

        session.setAttribute("userId", userServiceModel.getId());
        session.setAttribute("username", userServiceModel.getUsername());

        //return this.redirect("/home");

        //modelAndView.setViewName("redirect:/home");
        //return modelAndView;

        return super.redirect("/home");
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session){


        session.invalidate();

        //modelAndView.setViewName("redirect:/");
        //return modelAndView;
        return super.redirect("/");

    }


    */
}