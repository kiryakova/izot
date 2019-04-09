package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during user registration.")
public class UserRegisterException extends RuntimeException {

    public UserRegisterException(String message) {
        super(message);
    }
}
