package kiryakova.izot.domain.models.binding;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Category;
import kiryakova.izot.domain.entities.Producer;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductBindingModel {
    private String name;
    private String description;
    private BigDecimal price;
    private MultipartFile imageUrl;
    private Category category;
    private Producer producer;

    public ProductBindingModel() {
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    @Length(min = 5, max = 25, message = ConstantsDefinition.BindingModelConstants.NAME_IS_NOT_CORRECT)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    @DecimalMin("0.01")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}
