package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.repository.CategoryRepository;
import kiryakova.izot.repository.ProducerRepository;
import kiryakova.izot.repository.ProductRepository;
import kiryakova.izot.validation.ProductValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTests {
    @Autowired
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProducerRepository producerRepository;
    private ProductService productService;
    private CategoryService categoryService;
    private ProducerService producerService;
    private CloudinaryService cloudinaryService;
    private ProductValidationService productValidationService;
    private ModelMapper modelMapper;
    private Product product;
    private Category category;
    private Producer producer;
    private MultipartFile multipartFile;
    List<Product> products;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.productService = new ProductServiceImpl(this.productRepository, this.categoryService, this.producerService, this.cloudinaryService, this.productValidationService, this.modelMapper);

        product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setImageUrl("Image1.jpg");
        product.setPrice(new BigDecimal("0.01"));
        category = new Category();
        category.setName("Category1");
        category.setImageUrl("Image1.jpg");
        producer = new Producer();
        producer.setName("producer1");
        producer.setPhone("111");

        products = new ArrayList<>();
    }

    @Test
    public void getProducts_whenTwoProducts() {

        productRepository.deleteAll();
        productRepository.save(product);
        Product product1 = new Product();
        product1.setName("Product2");
        product1.setDescription("Description2");
        product1.setImageUrl("Image2.jpg");
        product1.setPrice(new BigDecimal("0.01"));
        category = new Category();
        category.setName("Category1");
        category.setImageUrl("Image2.jpg");
        producer = new Producer();
        producer.setName("producer1");
        producer.setPhone("111");

        productRepository.save(product);
        productRepository.save(product1);

        List<ProductServiceModel> productsFromDB = productService.findAllProducts();

        Assert.assertEquals(productsFromDB.size(), 2);
    }

    @Test
    public void productService_checkIfProductNameAlreadyExists() {

        product = productRepository.saveAndFlush(product);

        boolean exists = productService.checkIfProductNameAlreadyExists(product.getName());

        Assert.assertTrue(exists);
    }

    @Test
    public void productService_findProductByCategoryAndProducer() {

        product = productRepository.saveAndFlush(product);

        List<ProductServiceModel> products = productService.findAllProductsByCategoryIdAndProducerId("all", "all");

        Assert.assertNotNull(products);
    }

    @Test(expected = Exception.class)
    public void productService_addProduct() throws IOException {

        ProductServiceModel toBeSaved = new ProductServiceModel();
        toBeSaved.setName("Product1");
        toBeSaved.setDescription("Description1");
        toBeSaved.setImageUrl("Image1.jpg");
        toBeSaved.setPrice(new BigDecimal("0.01"));
        category = new Category();
        category.setName("Category1");
        toBeSaved.setCategory(modelMapper.map(category, CategoryServiceModel.class));
        producer = new Producer();
        producer.setName("producer1");
        producer.setPhone("111");
        toBeSaved.setProducer(modelMapper.map(producer, ProducerServiceModel.class));

        multipartFile = new MockMultipartFile(toBeSaved.getImageUrl(), new FileInputStream(new File("C:/test_img_files/Image1.jpg")));

        productService.addProduct(toBeSaved, multipartFile);

        Product actual = productRepository.findByName(toBeSaved.getName()).orElse(null);
        Product expected = productRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());

    }

    @Test(expected = Exception.class)
    public void productService_editProduct() throws IOException {

        product = productRepository.save(product);

        ProductServiceModel toBeEdited = new ProductServiceModel();
        toBeEdited.setName("Product2");
        toBeEdited.setDescription("Description2");
        toBeEdited.setImageUrl("Image2.jpg");
        toBeEdited.setPrice(new BigDecimal("0.01"));
        category = new Category();
        category.setName("Category1");
        toBeEdited.setCategory(modelMapper.map(category, CategoryServiceModel.class));
        producer = new Producer();
        producer.setName("producer1");
        producer.setPhone("111");
        toBeEdited.setProducer(modelMapper.map(producer, ProducerServiceModel.class));


        multipartFile = new MockMultipartFile(toBeEdited.getImageUrl(), new FileInputStream(new File("C:/test_img_files/Image2.jpg")));

        productService.editProduct(product.getId(), toBeEdited, multipartFile);

        Product actual = productRepository.findByName(toBeEdited.getName()).orElse(null);
        Product expected = productRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());

    }

    @Test(expected = Exception.class)
    public void productService_deleteProduct(){

        product = productRepository.save(product);

        productService.deleteProduct(product.getId());

        long expectedCount = 0;
        long actualCount = productRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void productService_deleteProductWithNullValues(){

        product = productRepository.save(product);

        productService.deleteProduct("InvalidId");

    }

    @Test(expected = Exception.class)
    public void productService_findByIdProductWithValidId() {

        product = productRepository.saveAndFlush(product);

        ProductServiceModel actual = productService.findProductById(product.getId());
        ProductServiceModel expected = this.modelMapper.map(product, ProductServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test(expected = Exception.class)
    public void productService_findByIdProductWithInValidId() {

        product = productRepository.saveAndFlush(product);

        productService.findProductById("InvalidId");
    }

}
