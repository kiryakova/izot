package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.CategoryServiceModel;
import kiryakova.izot.domain.models.service.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    void addProduct(ProductServiceModel productServiceModel, MultipartFile imageUrl);

    void editProduct(String id, ProductServiceModel productServiceModel, MultipartFile imageUrl);

    void deleteProduct(String id);

    ProductServiceModel findProductById(String id);

    List<ProductServiceModel> findAllProducts();

    List<ProductServiceModel> findAllProductsByCategoryId(String categoryId);

    boolean checkIfProductNameAlreadyExists(String name);

    //boolean setImageUrl(ProductServiceModel productServiceModel, MultipartFile multipartFile) throws IOException;
}
