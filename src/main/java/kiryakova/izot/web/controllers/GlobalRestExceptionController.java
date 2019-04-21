package kiryakova.izot.web.controllers;

import kiryakova.izot.error.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalRestExceptionController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    public @ResponseBody ErrorInfo handleRestSqlException(HttpServletRequest req,
                                                          Exception e) {
        return new ErrorInfo(req.getRequestURL().toString(), e);
    }
}
