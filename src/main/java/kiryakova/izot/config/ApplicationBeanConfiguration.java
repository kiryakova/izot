package kiryakova.izot.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Locale;


@Configuration
public class ApplicationBeanConfiguration {

    //@Value("${spring.activemq.broker-url}")
    //private String DEFAULT_BROKER_URL;

    /*static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        MappingsInitializer.initMappings(mapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }*/

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory
                = new ActiveMQConnectionFactory();

        activeMQConnectionFactory.setBrokerURL(DEFAULT_BROKER_URL);

        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());

        return jmsTemplate;
    }
    */

    @Bean
    public LocaleResolver localeResolver()  {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        //javaMailSender.setHost("smtp.mailtrap.io");
        //javaMailSender.setUsername("82101ff904438f");
        //javaMailSender.setPassword("2fe5682fa940fd");

        javaMailSender.setHost("smtp.mailtrap.io");
        javaMailSender.setUsername("abef134cf763e4");
        javaMailSender.setPassword("28fa4a7211a25b");
        javaMailSender.setPort(2525);

        return javaMailSender;
    }
}