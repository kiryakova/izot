package kiryakova.izot.domain.models.service;

import java.math.BigDecimal;

public class ProductServiceModel extends BaseServiceModel {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private CategoryServiceModel category;
    private ProducerServiceModel producer;

    public ProductServiceModel() {
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

    public CategoryServiceModel getCategory() {
        return category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }

    public ProducerServiceModel getProducer() {
        return producer;
    }

    public void setProducer(ProducerServiceModel producer) {
        this.producer = producer;
    }
}
