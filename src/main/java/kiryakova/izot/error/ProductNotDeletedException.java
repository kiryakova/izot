package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при изтриване на продукт!")
public class ProductNotDeletedException extends RuntimeException {
    private int statusCode;

    public ProductNotDeletedException() {
        this.statusCode = 400;
    }

    public ProductNotDeletedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
