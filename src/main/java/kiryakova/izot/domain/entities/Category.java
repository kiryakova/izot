package kiryakova.izot.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;
    private String imageUrl;
    //private List<Product> productList;

    public Category() {
    }

    @Column(name = "category_name",nullable = false, unique = true, updatable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
/*
    @OneToOne(targetEntity = Product.class, mappedBy = "category")
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
*/
}
