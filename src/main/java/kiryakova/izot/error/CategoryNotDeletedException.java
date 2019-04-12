package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при изтриване на категория!")
public class CategoryNotDeletedException extends RuntimeException {
    private int statusCode;

    public CategoryNotDeletedException() {
        this.statusCode = 400;
    }

    public CategoryNotDeletedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
