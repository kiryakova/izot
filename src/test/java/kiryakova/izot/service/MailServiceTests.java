package kiryakova.izot.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MailServiceTests {
    @Autowired
    private MailService mailService;
    private JavaMailSender javaMailSender;

    @Before
    public void init(){
        this.mailService = new MailServiceImpl(this.javaMailSender);
    }


    @Test
    public void mailService_sentRegistrationSuccessMessage() {

        mailService.sentRegistrationSuccessMessage("stelanz@abv.bg","stela");

    }
}
