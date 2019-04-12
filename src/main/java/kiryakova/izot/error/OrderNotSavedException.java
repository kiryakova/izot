package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при запис на поръчка!")
public class OrderNotSavedException extends RuntimeException {
    private int statusCode;

    public OrderNotSavedException() {
        this.statusCode = 400;
    }

    public OrderNotSavedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
