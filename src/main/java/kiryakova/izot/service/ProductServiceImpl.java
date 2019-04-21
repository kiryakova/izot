package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.error.ProductNotDeletedException;
import kiryakova.izot.error.ProductNotFoundException;
import kiryakova.izot.error.ProductNotSavedException;
import kiryakova.izot.repository.ProductRepository;
import kiryakova.izot.validation.ProductValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProducerService producerService;
    private final CloudinaryService cloudinaryService;
    private final ProductValidationService productValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryService categoryService,
                              ProducerService producerService,
                              CloudinaryService cloudinaryService,
                              ProductValidationService productValidation,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.producerService = producerService;
        this.cloudinaryService = cloudinaryService;
        this.productValidation = productValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addProduct(ProductServiceModel productServiceModel,
                           MultipartFile imageUrl) {
        if(!productValidation.isValid(productServiceModel)){
            throw new IllegalArgumentException();
        }

        Product product = this.modelMapper.map(productServiceModel, Product.class);

        this.setImageUrl(product, imageUrl);

        try {
            this.productRepository.save(product);
        } catch (Exception ignored){
            throw new ProductNotSavedException(
                    String.format(
                            ConstantsDefinition.ProductConstants.UNSUCCESSFUL_SAVED_PRODUCT,
                            product.getName())
            );
        }
    }

    @Override
    public void editProduct(String id,
                            ProductServiceModel productServiceModel,
                            MultipartFile imageUrl) {
        Product product = this.productRepository.findById(id).orElse(null);

        this.checkIfProductFound(product, productServiceModel.getName());

        productServiceModel.setCategory(
                this.categoryService
                        .findCategoryById(productServiceModel.getCategory().getId())
        );

        productServiceModel.setProducer(
                this.producerService
                        .findProducerById(productServiceModel.getProducer().getId())
        );

        if(!productValidation.isValid(productServiceModel)) {
            throw new IllegalArgumentException();
        }

        product.setName(productServiceModel.getName());
        product.setDescription(productServiceModel.getDescription());
        product.setPrice(productServiceModel.getPrice());
        product.setCategory(this.modelMapper
                .map(productServiceModel.getCategory(), Category.class));
        this.setImageUrl(product, imageUrl);

        try {
            this.productRepository.save(product);
        }catch (Exception ignored){
            throw new ProductNotSavedException(
                    String.format(ConstantsDefinition.ProductConstants.UNSUCCESSFUL_SAVED_PRODUCT,
                            product.getName())
            );
        }
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id).orElse(null);

        this.checkIfProductFound(product);

        try {
            this.productRepository.delete(product);
        }catch (Exception ignored){
            throw new ProductNotDeletedException(
                    String.format(ConstantsDefinition.ProductConstants.UNSUCCESSFUL_DELETE_PRODUCT,
                            product.getName())
            );
        }
    }

    @Override
    public ProductServiceModel findProductById(String id) {
        Product product = this.productRepository
                .findById(id).orElse(null);

        this.checkIfProductFound(product);

        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> findAllProducts() {
        return this.productRepository.findAllProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> findAllProductsByCategoryIdAndProducerId(String categoryId,
                                                                              String producerId) {

        if((categoryId.equals("all") || categoryId.isEmpty())
                && (producerId.equals("all") || producerId.isEmpty())) {
            return this.findAllProducts()
                    .stream()
                    .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                    .collect(Collectors.toList());
        }

        if(producerId.equals("all") || producerId.isEmpty()) {
            return this.productRepository
                    .findAllByCategoryId(categoryId)
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                    .collect(Collectors.toList());
        }

        if(categoryId.equals("all") || categoryId.isEmpty()) {
            return this.productRepository
                    .findAllByProducerId(producerId)
                    .stream()
                    .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                    .collect(Collectors.toList());
        }

        return this.productRepository
                .findAllByCategoryIdAndProducerId(categoryId, producerId)
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfProductNameAlreadyExists(String name) {
        Product product = this.productRepository
                .findByName(name).orElse(null);

        if(product == null) {
            return false;
        }

        return true;
    }

    private void setImageUrl(Product product, MultipartFile multipartFile) {
        try {
            product.setImageUrl(
                    this.cloudinaryService.uploadImage(multipartFile)
            );
        } catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    private void checkIfProductFound(Product product) {
        if(!productValidation.isValid(product)) {
            throw new ProductNotFoundException(ConstantsDefinition.ProductConstants.NO_SUCH_PRODUCT);
        }
    }

    private void checkIfProductFound(Product product, String name) {
        if(!productValidation.isValid(product)) {
            throw new ProductNotFoundException(String.format(ConstantsDefinition.ProductConstants.NO_PRODUCT_WITH_NAME, name));
        }
    }
}
