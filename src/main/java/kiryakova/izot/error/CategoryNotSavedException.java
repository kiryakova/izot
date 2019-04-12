package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при запис на категория!")
public class CategoryNotSavedException extends RuntimeException {
    private int statusCode;

    public CategoryNotSavedException() {
        this.statusCode = 400;
    }

    public CategoryNotSavedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
