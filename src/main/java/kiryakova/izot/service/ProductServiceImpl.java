package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.error.ProductNotFoundException;
import kiryakova.izot.error.ProductNotSavedException;
import kiryakova.izot.repository.ProductRepository;
import kiryakova.izot.validation.ProductValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final ProductValidationService productValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, CloudinaryService cloudinaryService, ProductValidationService productValidation, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.productValidation = productValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addProduct(ProductServiceModel productServiceModel) {
        if(!productValidation.isValid(productServiceModel)) {
            throw new ProductNotFoundException(ConstantsDefinition.ProductConstants.NO_SUCH_PRODUCT);
        }

        Product product = this.modelMapper.map(productServiceModel, Product.class);
        try {
            this.productRepository.save(product);
        } catch (Exception ignored){
            throw new ProductNotSavedException(ConstantsDefinition.ProductConstants.NO_SAVED_PRODUCT);
        }

        return true;
    }

    @Override
    public ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Product.class.getClass(), id)));

        productServiceModel.setCategory(
                this.categoryService.findCategoryById(productServiceModel.getCategory().getId())
        );

        if(!productValidation.isValid(productServiceModel)) {
            throw new IllegalArgumentException();
        }

        product.setName(productServiceModel.getName());
        product.setDescription(productServiceModel.getDescription());
        product.setPrice(productServiceModel.getPrice());
        product.setCategory(this.modelMapper.map(productServiceModel.getCategory(), Category.class));

        product = this.productRepository.saveAndFlush(product);
        return this.modelMapper.map(product, ProductServiceModel.class);

    }

    @Override
    public ProductServiceModel deleteProduct(String id) {
        Product product = this.productRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Product.class.getClass(), id))
                );

        this.productRepository.delete(product);

        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel findProductById(String id) {
        Product product = this.productRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Product.class.getClass(), id))
                );

        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> findAllProducts() {
        return this.productRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> findAllProductsByCategoryId(String categoryId) {

        if(categoryId.equals("all") || categoryId.isEmpty()) {
            return this.findAllProducts()
                    .stream()
                    .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                    .collect(Collectors.toList());
        }
        return this.productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean setImageUrl(ProductServiceModel productServiceModel, MultipartFile multipartFile) throws IOException {
        try {
            productServiceModel.setImageUrl(
                    this.cloudinaryService.uploadImage(multipartFile)
            );
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
