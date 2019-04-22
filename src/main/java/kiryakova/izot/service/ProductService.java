package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.ProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel productServiceModel,
                    MultipartFile imageUrl);

    void editProduct(String id,
                     ProductServiceModel productServiceModel,
                     MultipartFile imageUrl);

    void deleteProduct(String id);

    ProductServiceModel findProductById(String id);

    List<ProductServiceModel> findAllProducts();

    List<ProductServiceModel> findAllProductsByCategoryIdAndProducerId(String categoryId,
                                                                       String producerId);

    boolean checkIfProductNameAlreadyExists(String name);

}
