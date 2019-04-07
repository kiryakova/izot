package kiryakova.izot.config;

import kiryakova.izot.web.interceptors.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class ApplicationWebConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;
    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    public ApplicationWebConfig(LocaleChangeInterceptor localeChangeInterceptor, AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authorizationInterceptor).addPathPatterns("/", "/home");
        //registry.addInterceptor(this.authorizationInterceptor).addPathPatterns("/", "/home/**");
        //registry.addInterceptor(this.authorizationInterceptor);//all paths
        registry.addInterceptor(this.localeChangeInterceptor);
    }

}
