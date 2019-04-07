package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.CategoryViewModel;
import kiryakova.izot.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index() {
        return this.view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService
                .findAllCategories()
                .stream()
                .map(category -> this.modelMapper
                        .map(category, CategoryViewModel.class))
                .collect(Collectors.toList()));

        //modelAndView.addObject("username");

        return this.view("home", modelAndView);
    }

    /*

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("username", authentication.getName());

        //if(this.getPrincipalAuthority(authentication) != null
         //       && this.getPrincipalAuthority(authentication).equals("ADMIN")){
         //   return this.view("admin-home", modelAndView);
        //}

        return this.view("home", modelAndView);
}


    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.setViewName("/index");
        return modelAndView;
        //return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, HttpSession session){
        modelAndView.setViewName("home");
        //modelAndView.addObject("documents", documents);


        return modelAndView;
    }

    */
}
