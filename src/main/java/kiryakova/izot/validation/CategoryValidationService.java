package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;

public interface CategoryValidationService {
    boolean isValid(Category category);

    boolean isValid(CategoryServiceModel categoryServiceModel);
}
