package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during user profile edit.")
public class UserEditException extends RuntimeException {

    public UserEditException(String message) {
        super(message);
    }
}
