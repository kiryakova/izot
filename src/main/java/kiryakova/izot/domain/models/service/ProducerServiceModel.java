package kiryakova.izot.domain.models.service;

import kiryakova.izot.common.ConstantsDefinition;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProducerServiceModel extends BaseServiceModel {
    private String name;
    private String phone;

    public ProducerServiceModel() {
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
