package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при изтриване на производител!")
public class ProducerNotDeletedException extends RuntimeException {
    private int statusCode;

    public ProducerNotDeletedException() {
        this.statusCode = 400;
    }

    public ProducerNotDeletedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
