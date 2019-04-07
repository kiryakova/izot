package kiryakova.izot.validation.implementations;

import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import kiryakova.izot.validation.ProductValidationService;
import org.springframework.stereotype.Component;

@Component
public class ProductValidationServiceImpl implements ProductValidationService {
    @Override
    public boolean isValid(Product product) {
        return product != null;
    }

    @Override
    public boolean isValid(ProductServiceModel productServiceModel) {
        return productServiceModel != null
                && isCategoryValid(productServiceModel.getCategory());
    }

    private boolean isCategoryValid(CategoryServiceModel categoryServiceModel) {
        return categoryServiceModel != null;
    }
    /*
    @Override
    public boolean isValid(ProductServiceModel product) {
        return product != null
                && areCategoriesValid(product.getCategories()));
    }

    private boolean areCategoriesValid(List<CategoryServiceModel> categories) {
        return categories != null && !categories.isEmpty();
    }*/
}
