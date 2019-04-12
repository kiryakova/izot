package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Product;
import kiryakova.izot.domain.models.service.ProductServiceModel;

public interface ProductValidationService {
    boolean isValid(Product product);

    boolean isValid(ProductServiceModel productServiceModel);
}
