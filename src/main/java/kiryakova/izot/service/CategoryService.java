package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CategoryServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryServiceModel categoryServiceModel, MultipartFile imageUrl);

    void editCategory(String id, CategoryServiceModel categoryServiceModel, MultipartFile imageUrl);

    void deleteCategory(String id);

    CategoryServiceModel findCategoryById(String id);

    List<CategoryServiceModel> findAllCategories();

    boolean checkIfCategoryNameAlreadyExists(String name);

}
