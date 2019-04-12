package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Продуктът не е намерен!")
public class OrderProductNotFoundException extends RuntimeException {
    private int statusCode;

    public OrderProductNotFoundException() {
        this.statusCode = 404;
    }

    public OrderProductNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
