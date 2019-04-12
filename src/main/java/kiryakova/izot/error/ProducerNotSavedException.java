package kiryakova.izot.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Грешка при запис на производител!")
public class ProducerNotSavedException extends RuntimeException {
    private int statusCode;

    public ProducerNotSavedException() {
        this.statusCode = 400;
    }

    public ProducerNotSavedException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
