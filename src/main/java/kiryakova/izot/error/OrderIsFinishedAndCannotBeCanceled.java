package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Поръчката е приключена и не може да бъде отказана!")
public class OrderIsFinishedAndCannotBeCanceled extends RuntimeException {
    private int statusCode;

    public OrderIsFinishedAndCannotBeCanceled() {
        this.statusCode = 409;
    }

    public OrderIsFinishedAndCannotBeCanceled(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
