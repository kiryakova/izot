package kiryakova.izot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class IzotApplication {

    public static void main(String[] args) {
        SpringApplication.run(IzotApplication.class, args);
    }

}
