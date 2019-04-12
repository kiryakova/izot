package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при запис на клиент!")
public class CustomerNotSavedException extends RuntimeException {
    private int statusCode;

    public CustomerNotSavedException() {
        this.statusCode = 400;
    }

    public CustomerNotSavedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
