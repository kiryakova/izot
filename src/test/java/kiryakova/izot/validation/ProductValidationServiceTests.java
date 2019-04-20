package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;

public class ProductValidationServiceTests {
    private ProductValidationService productValidationService;

    @Before
    public void init() {
        productValidationService = new ProductValidationServiceImpl();
    }

    @Test
    public void isValidWithProduct_whenValid_true() {
        Product product = new Product();
        product.setCategory(new Category());
        product.setProducer(new Producer());
        boolean result = productValidationService.isValid(product);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithProduct_whenNull_false() {
        Product product = null;
        boolean result = productValidationService.isValid(product);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithProductServiceModel_whenValidTrue() {
        ProductServiceModel productServiceModel = new ProductServiceModel();
        productServiceModel.setCategory(new CategoryServiceModel());
        productServiceModel.setProducer(new ProducerServiceModel());
        boolean result = productValidationService.isValid(productServiceModel);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithProductServiceModel_whenNull_false() {
        ProductServiceModel productServiceModel = null;
        boolean result = productValidationService.isValid(productServiceModel);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithProductServiceModel_whenNullCategory_false() {
        ProductServiceModel productServiceModel = new ProductServiceModel();
        productServiceModel.setCategory(null);
        productServiceModel.setProducer(new ProducerServiceModel());
        boolean result = productValidationService.isValid(productServiceModel);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithProductServiceModel_whenNullProducer_false() {
        ProductServiceModel productServiceModel = new ProductServiceModel();
        productServiceModel.setCategory(new CategoryServiceModel());
        productServiceModel.setProducer(null);
        boolean result = productValidationService.isValid(productServiceModel);
        Assert.assertFalse(result);
    }
}
