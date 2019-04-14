package kiryakova.izot.domain.models.view;

import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.models.service.CategoryServiceModel;

import java.math.BigDecimal;

public class ProductDetailsViewModel extends BaseViewModel {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private CategoryViewModel category;
    private ProducerViewModel producer;

    public ProductDetailsViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public CategoryViewModel getCategory() {
        return category;
    }

    public void setCategory(CategoryViewModel category) {
        this.category = category;
    }

    public ProducerViewModel getProducer() {
        return producer;
    }

    public void setProducer(ProducerViewModel producer) {
        this.producer = producer;
    }
}
