package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CategoryServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    void addCategory(CategoryServiceModel categoryServiceModel, MultipartFile imageUrl);

    void editCategory(String id, CategoryServiceModel categoryServiceModel, MultipartFile imageUrl);

    void deleteCategory(String id);

    CategoryServiceModel findCategoryById(String id);

    List<CategoryServiceModel> findAllCategories();

    //void setImageUrl(CategoryServiceModel categoryServiceModel, MultipartFile multipartFile);

    boolean checkIfCategoryNameAlreadyExists(String name);

}
