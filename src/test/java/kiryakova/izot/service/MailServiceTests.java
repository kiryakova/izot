package kiryakova.izot.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class MailServiceTests {
    @Mock
    private MailServiceImpl mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Before
    public void init(){
        this.mailService = new MailServiceImpl(this.javaMailSender);
    }


    @Test
    public void mailService_sentRegistrationSuccessMessage() {

        //this.mailService.sentRegistrationSuccessMessage("stelanz@abv.bg","stela");

    }
}
