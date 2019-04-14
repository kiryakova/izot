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
public class ProductAppController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductAppController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    //@GetMapping(value = {"/fetch", "/fetch/{categoryId}", "/fetch/{categoryId}{producerId}"})
    @RequestMapping(
            value = "/products/fetch/",
            params = { "categoryId", "producerId" },
            method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    //@ResponseBody
    public List<ProductAllViewModel> fetchByCategory(@RequestParam(name = "categoryId", required = false) String categoryId,
                                                     @RequestParam(name = "producerId", required = false) String producerId) {

        return this.productService.findAllProductsByCategoryIdAndProducerId(categoryId, producerId)
                .stream()
                .map(product -> this.modelMapper.map(product, ProductAllViewModel.class))
                .collect(Collectors.toList());
    }
}
