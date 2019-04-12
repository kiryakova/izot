package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Производителят не е намерена!")
public class ProducerNotFoundException extends RuntimeException {
    private int statusCode;

    public ProducerNotFoundException() {
        this.statusCode = 404;
    }

    public ProducerNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
