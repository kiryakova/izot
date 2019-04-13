package kiryakova.izot.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //return super.preHandle(request, response, handler);

        //System.out.println(((HandlerMethod) handler).getMethodAnnotation(GetMapping.class));
        //return true;

        ////нещо си ако не е изпълнено отиди на login и върни false, т.е. прекъани заявката и препрати на login
        //response.sendRedirect("/login");
        //return false;

        //boolean a = ((HandlerMethod) handler).getMethod().isAnnotationPresent(GetMapping.class);
        //System.out.println(response.getStatus());
        //return true;
        if(response.getStatus() == 403){
            response.sendRedirect("unauthorized");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //super.postHandle(request, response, handler, modelAndView);
        //<h1 th:text="|${httpStatus} - ${httpStatus.reasonPhrase}|">404</h1>

        if(response.getStatus() == 403){
            response.sendRedirect("unauthorized");
        }

    }


}
