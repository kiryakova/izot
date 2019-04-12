package kiryakova.izot.domain.models.binding;

import kiryakova.izot.common.ConstantsDefinition;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategoryBindingModel {
    private String name;
    private MultipartFile imageUrl;

    public CategoryBindingModel() {
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

    //@NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(MultipartFile imageUrl) {
        this.imageUrl = imageUrl;
    }
}
