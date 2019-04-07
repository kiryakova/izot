package kiryakova.izot.domain.models.binding;

import kiryakova.izot.common.ConstantsDefinition;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegisterBindingModel {
    private String username;
    private String email;
    //@ValidPassword
    private String password;
    //@ValidPassword
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    //@Email(message = ConstantsDefinition.UserBindingModelConstants.EMAIL_IS_NOT_CORRECT)
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(message = ConstantsDefinition.BindingModelConstants.NOT_NULL)
    @NotEmpty(message = ConstantsDefinition.BindingModelConstants.NOT_EMPTY)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
