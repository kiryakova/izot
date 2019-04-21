package kiryakova.izot.domain.models.binding;

import kiryakova.izot.common.ConstantsDefinition;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CustomerBindingModel {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public CustomerBindingModel() {
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    @Length(min = 2, message = ConstantsDefinition.BindingModelConstants.CUSTOMER_NAME_IS_NOT_CORRECT)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    @Length(min = 2, message = ConstantsDefinition.BindingModelConstants.CUSTOMER_NAME_IS_NOT_CORRECT)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
