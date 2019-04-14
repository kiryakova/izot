package kiryakova.izot.config;

import kiryakova.izot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    /*private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.cors().disable()  //когато е готово да го закомянтаря
                //.csrf().disable()  //когато е готово да го закомянтаря, да е налично само докато работя по проекта. Posle dolnoto
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository())
                .and()
                    .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    .antMatchers("/", "/index", "/users/login", "/users/register").anonymous()
                    //.antMatchers("/admin/**").hasAuthority("ADMIN")
                    //.antMatchers("/products/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/users/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/home")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    //.logoutSuccessUrl("/users/login?logout").permitAll()
                    //.logoutSuccessUrl("/?logout").permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/unauthorized");

        /*
        .and()
                    .rememberMe()
                    .rememberMeParameter("rememberMe")
                    .key("PLYOK")
                    .userDetailsService(this.userService)
                    .rememberMeCookieName("KLYOK")
                    .tokenValiditySeconds(1200);
                */
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

}
