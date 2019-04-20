package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryValidationServiceTests {
    private CategoryValidationService categoryValidationService;

    @Before
    public void init() {
        categoryValidationService = new CategoryValidationServiceImpl();
    }

    @Test
    public void isValidWithCategory_whenValid_true() {
        Category category = new Category();
        boolean result = categoryValidationService.isValid(category);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithCategory_whenNull_false() {
        Category category = null;
        boolean result = categoryValidationService.isValid(category);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithCategoryServiceModel_whenValid_false() {
        CategoryServiceModel categoryServiceModel = new CategoryServiceModel();
        boolean result = categoryValidationService.isValid(categoryServiceModel);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithCategoryServiceModel_whenNull_false() {
        CategoryServiceModel categoryServiceModel = null;
        boolean result = categoryValidationService.isValid(categoryServiceModel);
        Assert.assertFalse(result);
    }
}
