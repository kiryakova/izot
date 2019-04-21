package kiryakova.izot.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController extends BaseController {

    @ExceptionHandler({Throwable.class})
    public ModelAndView handleSqlException(Throwable e) {
        ModelAndView modelAndView = new ModelAndView("error");

        Throwable throwable = e;

        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }

        modelAndView.addObject("message",
                throwable.getMessage());
        modelAndView.addObject("reason",
                throwable.getClass().isAnnotationPresent(ResponseStatus.class)
                ? throwable.getClass().getAnnotation(ResponseStatus.class).reason()
                : "");

        modelAndView.addObject("status",
                throwable.getClass().isAnnotationPresent(ResponseStatus.class)
                ? throwable.getClass().getAnnotation(ResponseStatus.class).value()
                : "");

        return modelAndView;
    }
}
