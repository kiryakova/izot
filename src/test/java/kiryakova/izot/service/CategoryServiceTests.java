package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.repository.CategoryRepository;
import kiryakova.izot.validation.CategoryValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTests {
    @Autowired
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;
    private CloudinaryService cloudinaryService;
    private CategoryValidationService categoryValidationService;
    private ModelMapper modelMapper;
    private Category category;
    private MultipartFile multipartFile;
    List<Category> categories;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.categoryService = new CategoryServiceImpl(this.categoryRepository, this.cloudinaryService, this.categoryValidationService, this.modelMapper);

        category = new Category();
        category.setName("Category1");
        category.setImageUrl("Image1.jpg");

        categories = new ArrayList<>();
    }

    @Test
    public void getCategories_whenTwoCategories() {

        categoryRepository.deleteAll();
        categoryRepository.save(category);
        Category category1 = new Category();
        category1.setName("category2");
        category1.setImageUrl("Image2.jpg");

        this.categoryRepository.save(category);
        this.categoryRepository.save(category1);

        List<CategoryServiceModel> categoriesFromDB = categoryService.findAllCategories();

        Assert.assertEquals(categoriesFromDB.size(), 2);
    }

    @Test
    public void categoryService_checkIfCategoryNameAlreadyExists() {

        category = categoryRepository.saveAndFlush(category);

        boolean exists = categoryService.checkIfCategoryNameAlreadyExists(category.getName());

        Assert.assertTrue(exists);
    }

    @Test(expected = Exception.class)
    public void categoryService_addCategory() throws IOException {

        CategoryServiceModel toBeSaved = new CategoryServiceModel();
        toBeSaved.setName("Category1");
        toBeSaved.setImageUrl("Image1.jpg");

        multipartFile = new MockMultipartFile(toBeSaved.getImageUrl(), new FileInputStream(new File("C:/test_img_files/Image1.jpg")));

        categoryService.addCategory(toBeSaved, multipartFile);

        Category actual = this.categoryRepository.findByName(toBeSaved.getName()).orElse(null);
        Category expected = this.categoryRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());

    }

    @Test(expected = Exception.class)
    public void categoryService_editCategory() throws IOException {

        category = this.categoryRepository.save(category);

        CategoryServiceModel toBeEdited = new CategoryServiceModel();
        toBeEdited.setId(category.getId());
        toBeEdited.setName("Category2");
        toBeEdited.setImageUrl("Image2.jpg");

        multipartFile = new MockMultipartFile(toBeEdited.getImageUrl(), new FileInputStream(new File("C:/test_img_files/Image2.jpg")));

        categoryService.editCategory(category.getId(), toBeEdited, multipartFile);

        Category actual = this.categoryRepository.findByName(toBeEdited.getName()).orElse(null);
        Category expected = this.categoryRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());

    }

    @Test(expected = Exception.class)
    public void categoryService_deleteCategory(){

        category = this.categoryRepository.save(category);

        categoryService.deleteCategory(category.getId());

        long expectedCount = 0;
        long actualCount = this.categoryRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void categoryService_deleteCategoryWithNullValues(){

        category = this.categoryRepository.save(category);

        categoryService.deleteCategory("InvalidId");

    }

    @Test(expected = Exception.class)
    public void categoryService_findByIdCategoryWithValidId() {

        category = this.categoryRepository.saveAndFlush(category);

        CategoryServiceModel actual = categoryService.findCategoryById(category.getId());
        CategoryServiceModel expected = this.modelMapper.map(category, CategoryServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    @Test(expected = Exception.class)
    public void categoryService_findByIdCategoryWithInValidId() {

        category = this.categoryRepository.saveAndFlush(category);

        categoryService.findCategoryById("InvalidId");
    }
}
