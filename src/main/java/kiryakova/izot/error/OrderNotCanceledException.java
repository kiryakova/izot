package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при отказване на поръчка!")
public class OrderNotCanceledException extends RuntimeException {
    private int statusCode;

    public OrderNotCanceledException() {
        this.statusCode = 400;
    }

    public OrderNotCanceledException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
