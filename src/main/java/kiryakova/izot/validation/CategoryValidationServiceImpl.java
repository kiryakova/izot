package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidationServiceImpl implements CategoryValidationService {
    @Override
    public boolean isValid(Category category) {
        return category != null;
    }

    @Override
    public boolean isValid(CategoryServiceModel categoryServiceModel) {
        return categoryServiceModel != null;
    }
}
