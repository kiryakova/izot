package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CategoryServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);

    CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel);

    CategoryServiceModel deleteCategory(String id);

    CategoryServiceModel findCategoryById(String id);

    List<CategoryServiceModel> findAllCategories();

    void setImageUrl(CategoryServiceModel categoryServiceModel, MultipartFile multipartFile) throws IOException;
}
