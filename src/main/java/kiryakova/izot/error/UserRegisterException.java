package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при регистрация на потребител!")
public class UserRegisterException extends RuntimeException {

    private int statusCode;

    public UserRegisterException() {
        this.statusCode = 400;
    }

    public UserRegisterException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
