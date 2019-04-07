package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        Category category = this.modelMapper.map(categoryServiceModel, Category.class);

        this.categoryRepository.saveAndFlush(category);

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Category.class.getClass(), id))
                );

        category.setName(categoryServiceModel.getName());
        category.setImageUrl(categoryServiceModel.getImageUrl());

        category = this.categoryRepository.saveAndFlush(category);
        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel deleteCategory(String id) {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Category.class.getClass(), id))
                );

        this.categoryRepository.delete(category);

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel findCategoryById(String id) {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Category.class.getClass(), id))
                );

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
    public void setImageUrl(CategoryServiceModel categoryServiceModel, MultipartFile multipartFile) throws IOException {
        categoryServiceModel.setImageUrl(
                this.cloudinaryService.uploadImage(multipartFile)
        );
    }

}
