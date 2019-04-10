package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при запис на продукт!")
public class ProductNotSavedException extends RuntimeException {

    private int statusCode;

    public ProductNotSavedException() {
        this.statusCode = 400;
    }

    public ProductNotSavedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
