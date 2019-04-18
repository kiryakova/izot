package kiryakova.izot.web.interceptors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        String messageWelcome = "";

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }

        if (handler instanceof HandlerMethod) {
            PreAuthorize methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(PreAuthorize.class);

            if(methodAnnotation != null){
                if(!methodAnnotation.value().equals("isAnonymous()")){
                    messageWelcome += "Здравейте, ";
                }
            }

            modelAndView
                    .addObject("messageWelcome", messageWelcome);
        }
    }
}
