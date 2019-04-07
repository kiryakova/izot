package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.binding.CategoryBindingModel;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.view.CategoryViewModel;
import kiryakova.izot.service.CategoryService;
import kiryakova.izot.service.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("category", new CategoryBindingModel());
        return this.view("category/add-category", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addCategoryConfirm(ModelAndView modelAndView,
                                    @ModelAttribute(name = "category") @Valid CategoryBindingModel categoryBindingModel,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("category", categoryBindingModel);
            return this.view("category/add-category", modelAndView);
        }

        CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class);

        /*categoryServiceModel.setImageUrl(
                this.cloudinaryService.uploadImage(categoryBindingModel.getImageUrl())
        );*/

        try {
            categoryService.setImageUrl(categoryServiceModel, categoryBindingModel.getImageUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.categoryService.addCategory(categoryServiceModel);

        return this.redirect("/categories/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView editCategory(@PathVariable String id, ModelAndView modelAndView) {
        CategoryServiceModel categoryServiceModel = this.categoryService.findCategoryById(id);
        CategoryBindingModel categoryBindingModel = this.modelMapper.map(categoryServiceModel, CategoryBindingModel.class);

        modelAndView.addObject("category", categoryBindingModel);
        modelAndView.addObject("categoryId", id);
        modelAndView.addObject("imgUrlStr", categoryServiceModel.getImageUrl());

        return this.view("category/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView editCategoryConfirm(ModelAndView modelAndView,
                                     @PathVariable String id,
                                     @ModelAttribute(name = "category") @Valid CategoryBindingModel categoryBindingModel,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("category", categoryBindingModel);
            return this.view("category/edit-category", modelAndView);
        }

        CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class);

        /*categoryServiceModel.setImageUrl(
                this.cloudinaryService.uploadImage(categoryBindingModel.getImageUrl())
        );*/

        try {
            categoryService.setImageUrl(categoryServiceModel, categoryBindingModel.getImageUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.categoryService.editCategory(id, categoryServiceModel);

        return this.redirect("/categories/all");
        //return super.redirect("/producers/details/" + id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView deleteCategory(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("category",
                this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class)
        );

        modelAndView.addObject("categoryId", id);

        return this.view("category/delete-category", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id) {
        this.categoryService.deleteCategory(id);

        return this.redirect("/categories/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView allCategories(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService.findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList()));

        return this.view("category/all-categories", modelAndView);
    }

}
