package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при изтриване на заявен продукт!")
public class OrderProductNotDeletedException extends RuntimeException {
    private int statusCode;

    public OrderProductNotDeletedException() {
        this.statusCode = 400;
    }

    public OrderProductNotDeletedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
