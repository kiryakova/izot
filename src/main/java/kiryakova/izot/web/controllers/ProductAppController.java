package kiryakova.izot.web.controllers;

import kiryakova.izot.domain.models.view.ProductAllViewModel;
import kiryakova.izot.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductAppController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductAppController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = {"/fetch", "/fetch/{categoryId}"})
    @PreAuthorize("isAuthenticated()")
    //@ResponseBody
    public List<ProductAllViewModel> fetchByCategory(@PathVariable String categoryId) {

        return this.productService.findAllProductsByCategoryId(categoryId)
                .stream()
                .map(product -> this.modelMapper.map(product, ProductAllViewModel.class))
                .collect(Collectors.toList());
    }
}
