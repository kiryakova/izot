package kiryakova.izot.domain.models.view;

import kiryakova.izot.domain.entities.Category;

import java.math.BigDecimal;

public class ProductAllViewModel extends BaseViewModel {
    private String name;
    private BigDecimal price;
    private String imageUrl;

    public ProductAllViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
