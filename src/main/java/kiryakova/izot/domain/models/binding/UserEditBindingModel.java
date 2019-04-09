package kiryakova.izot.domain.models.binding;

import kiryakova.izot.common.ConstantsDefinition;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserEditBindingModel {
    private String username;
    private String email;
    //@ValidPassword
    private String oldPassword;
    //@ValidPassword
    private String password;
    //@ValidPassword
    private String confirmPassword;

    public UserEditBindingModel() {
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
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = ConstantsDefinition.BindingModelConstants.EMAIL_IS_NOT_CORRECT)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 3, max = 18, message = ConstantsDefinition.BindingModelConstants.PASSWORD_IS_NOT_CORRECT)
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Length(min = 3, max = 18, message = ConstantsDefinition.BindingModelConstants.PASSWORD_IS_NOT_CORRECT)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min = 3, max = 18, message = ConstantsDefinition.BindingModelConstants.PASSWORD_IS_NOT_CORRECT)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
