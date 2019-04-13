package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.CategoryViewModel;
import kiryakova.izot.service.CategoryService;
import kiryakova.izot.web.annotations.PageTitle;
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
    //@PageTitle("Начало")
    public ModelAndView index() {
        return this.view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Начало")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService
                .findAllCategories()
                .stream()
                .map(category -> this.modelMapper
                        .map(category, CategoryViewModel.class))
                .collect(Collectors.toList()));

        return this.view("home", modelAndView);
    }

}
