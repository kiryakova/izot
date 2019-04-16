package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;

public class ProductValidationServiceTests {
    private ProductValidationService productValidationService;

    @Before
    public void setupTest() {
        productValidationService = new ProductValidationServiceImpl();
    }

    @Test
    public void isValidWithProduct_whenValid_true() {
        Product product = new Product();
        product.setCategory(new Category());
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
    public void isValidWithProductServiceModel_whenNull_false() {
        ProductServiceModel product = null;
        boolean result = productValidationService.isValid(product);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithProductServiceModel_whenValid_true() {
        ProductServiceModel product = new ProductServiceModel();
        product.setCategory(new CategoryServiceModel());
        boolean result = productValidationService.isValid(product);
        Assert.assertFalse(result);
    }

    @Test
    public void t1() {
        ProductServiceModel product = new ProductServiceModel();
        product.setCategory(null);

        boolean result = productValidationService.isValid(product);
        Assert.assertFalse(result);
    }


    @Test
    public void t2() {
        ProductServiceModel product = new ProductServiceModel();
        product.setCategory(new CategoryServiceModel());

        boolean result = productValidationService.isValid(product);
        Assert.assertFalse(result);
    }
}
