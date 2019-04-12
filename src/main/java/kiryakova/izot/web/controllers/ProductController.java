package kiryakova.izot.web.controllers;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.models.binding.ProductBindingModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.domain.models.view.ProductAllViewModel;
import kiryakova.izot.domain.models.view.ProductDetailsViewModel;
import kiryakova.izot.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addProduct(ModelAndView modelAndView) {
        modelAndView.addObject("product", new ProductBindingModel());
        return this.view("product/add-product", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView addProductConfirm(ModelAndView modelAndView,
                                          @ModelAttribute(name = "product") @Valid ProductBindingModel productBindingModel,
                                          BindingResult bindingResult) {

        if(this.productService.checkIfProductNameAlreadyExists(productBindingModel.getName())){
            bindingResult.addError(new FieldError("productBindingModel", "name",
                    String.format(ConstantsDefinition.ProductConstants.PRODUCT_ALREADY_EXISTS, productBindingModel.getName())));
        }

        if(productBindingModel.getImageUrl() == null || productBindingModel.getImageUrl().isEmpty()){
            bindingResult.addError(new FieldError("productBindingModel", "imageUrl",
                    ConstantsDefinition.BindingModelConstants.NOT_EMPTY));
        }

        if(bindingResult.hasErrors()) {
            modelAndView.addObject("product", productBindingModel);
            return this.view("product/add-product", modelAndView);
        }

        ProductServiceModel productServiceModel = this.modelMapper.map(productBindingModel, ProductServiceModel.class);

        this.productService.addProduct(productServiceModel, productBindingModel.getImageUrl());

        return this.redirect("/products/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView)  {
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductBindingModel productBindingModel = this.modelMapper.map(productServiceModel, ProductBindingModel.class);

        modelAndView.addObject("product", productBindingModel);
        modelAndView.addObject("productId", id);
        modelAndView.addObject("imgUrlStr", productServiceModel.getImageUrl());

        return this.view("product/edit-product", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView editProductConfirm(ModelAndView modelAndView,
                                            @PathVariable String id,
                                            @ModelAttribute(name = "product") @Valid ProductBindingModel productBindingModel,
                                            BindingResult bindingResult) {

        /*if(this.productService.checkIfProductNameAlreadyExists(productBindingModel.getName())){
            bindingResult.addError(new FieldError("productBindingModel", "name",
                    String.format(ConstantsDefinition.ProductConstants.PRODUCT_ALREADY_EXISTS, productBindingModel.getName())));
        }*/

        if(productBindingModel.getImageUrl() == null || productBindingModel.getImageUrl().isEmpty()){
            bindingResult.addError(new FieldError("productBindingModel", "imageUrl",
                    ConstantsDefinition.BindingModelConstants.NOT_EMPTY));
        }

        if(bindingResult.hasErrors()) {
            modelAndView.addObject("product", productBindingModel);
            modelAndView.addObject("productId", id);
            modelAndView.addObject("imgUrlStr", productBindingModel.getImageUrl());

            return this.view("product/edit-product", modelAndView);
        }

        ProductServiceModel productServiceModel = this.modelMapper.map(productBindingModel, ProductServiceModel.class);

        this.productService.editProduct(id, productServiceModel, productBindingModel.getImageUrl());

        return this.redirect("/products/details/" + id);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("product", this.modelMapper.map(this.productService.findProductById(id), ProductDetailsViewModel.class));

        return this.view("product/details-product", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("product",
                this.modelMapper.map(this.productService.findProductById(id), ProductDetailsViewModel.class)
        );

        modelAndView.addObject("productId", id);

        return this.view("product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {
        this.productService.deleteProduct(id);

        return this.redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ModelAndView allProductsUpFromModerator(ModelAndView modelAndView) {
        modelAndView.addObject("products", this.productService.findAllProducts()
                .stream()
                .map(c -> this.modelMapper.map(c, ProductAllViewModel.class))
                .collect(Collectors.toList()));

        return this.view("product/all-products", modelAndView);
    }

    @GetMapping(value = {"/all/products", "/all/products/{categoryId}"})
    @PreAuthorize("hasAuthority('USER')")
    public ModelAndView allProducts(@PathVariable(name="categoryId", required=false) String categoryId, ModelAndView modelAndView) {

        modelAndView.addObject("categoryId", categoryId != null && !categoryId.equals("") ? categoryId : "all");

        modelAndView.addObject("products", this.productService.findAllProducts()
                .stream()
                .map(c -> this.modelMapper.map(c, ProductAllViewModel.class))
                .collect(Collectors.toList()));

        return this.view("product/all", modelAndView);
    }
}

