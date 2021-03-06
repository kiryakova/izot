package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при редактиране профила на потребител!")
public class UserEditException extends RuntimeException {

    private int statusCode;

    public UserEditException() {
        this.statusCode = 400;
    }

    public UserEditException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

