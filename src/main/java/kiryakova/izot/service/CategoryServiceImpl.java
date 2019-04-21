package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.error.CategoryNotDeletedException;
import kiryakova.izot.error.CategoryNotFoundException;
import kiryakova.izot.error.CategoryNotSavedException;
import kiryakova.izot.repository.CategoryRepository;
import kiryakova.izot.validation.CategoryValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryValidationService categoryValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CloudinaryService cloudinaryService,
                               CategoryValidationService categoryValidation,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.categoryValidation = categoryValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCategory(CategoryServiceModel categoryServiceModel,
                            MultipartFile imageUrl) {
        if(!categoryValidation.isValid(categoryServiceModel)){
            throw new IllegalArgumentException();
        }

        Category category = this.modelMapper.map(categoryServiceModel, Category.class);

        this.setImageUrl(category, imageUrl);

        try {
            this.categoryRepository.save(category);
        } catch (Exception ignored){
            throw new CategoryNotSavedException(
                    String.format(
                            ConstantsDefinition.CategoryConstants.UNSUCCESSFUL_SAVED_CATEGORY,
                            category.getName())
            );
        }
    }

    @Override
    public void editCategory(String id, CategoryServiceModel categoryServiceModel,
                             MultipartFile imageUrl) {
        Category category = this.categoryRepository.findById(id).orElse(null);

        this.checkIfCategoryFound(category, categoryServiceModel.getName());

        if(!categoryValidation.isValid(categoryServiceModel)) {
            throw new IllegalArgumentException();
        }

        category.setName(categoryServiceModel.getName());
        this.setImageUrl(category, imageUrl);

        try {
            this.categoryRepository.save(category);
        }catch (Exception ignored){
            throw new CategoryNotSavedException(
                    String.format(
                            ConstantsDefinition.CategoryConstants.UNSUCCESSFUL_SAVED_CATEGORY,
                            category.getName())
            );
        }
    }

    @Override
    public void deleteCategory(String id) {
        Category category = this.categoryRepository.findById(id).orElse(null);

        this.checkIfCategoryFound(category);

        try {
            this.categoryRepository.delete(category);
        }catch (Exception ignored){
            throw new CategoryNotDeletedException(
                    String.format(
                            ConstantsDefinition.CategoryConstants.UNSUCCESSFUL_DELETE_CATEGORY,
                            category.getName())
            );
        }
    }

    @Override
    public CategoryServiceModel findCategoryById(String id) {
        Category category = this.categoryRepository.findById(id).orElse(null);

        this.checkIfCategoryFound(category);

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfCategoryNameAlreadyExists(String name) {
        Category category = this.categoryRepository
                .findByName(name).orElse(null);

        if(category == null) {
            return false;
        }

        return true;
    }

    private void setImageUrl(Category category, MultipartFile multipartFile) {
        try {
            category.setImageUrl(
                    this.cloudinaryService.uploadImage(multipartFile)
            );
        } catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    private void checkIfCategoryFound(Category category) {
        if(!categoryValidation.isValid(category)) {
            throw new CategoryNotFoundException(
                    ConstantsDefinition.CategoryConstants.NO_SUCH_CATEGORY);
        }
    }

    private void checkIfCategoryFound(Category category, String name) {
        if(!categoryValidation.isValid(category)) {
            throw new CategoryNotFoundException(
                    String.format(
                            ConstantsDefinition.CategoryConstants.NO_CATEGORY_WITH_NAME,
                            name));
        }
    }

}
