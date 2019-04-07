package kiryakova.izot.domain.models.service;

import kiryakova.izot.domain.entities.Category;

import java.math.BigDecimal;

public class ProductServiceModel extends BaseServiceModel {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    //private Producer producer;
    private CategoryServiceModel category;

    /*public ProductServiceModel() {
    }*/

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
/*
    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
*/

    public CategoryServiceModel getCategory() {
        return category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }
}
