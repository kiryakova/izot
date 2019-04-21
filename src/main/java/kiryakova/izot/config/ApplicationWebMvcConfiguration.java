package kiryakova.izot.config;

import kiryakova.izot.web.interceptors.AuthorizationInterceptor;
import kiryakova.izot.web.interceptors.TitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {
    private final TitleInterceptor titleInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    @Autowired
    public ApplicationWebMvcConfiguration(TitleInterceptor titleInterceptor,
                                          AuthorizationInterceptor authorizationInterceptor) {
        this.titleInterceptor = titleInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.titleInterceptor);
        registry.addInterceptor(this.authorizationInterceptor);
    }

}
