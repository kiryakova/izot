package kiryakova.izot.validation;

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
}
