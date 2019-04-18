package kiryakova.izot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private static final String REGISTRATION_MAIL_SUBJECT =
            "ИЗОТ - Регистрация";

    private static final String REGISTRATION_SUCCESS_MESSAGE
            = "Добре дошли, %s!%n" + " Успешно се регистрирахте в сайта на ИЗОТ Н.Загора.";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sentRegistrationSuccessMessage(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(REGISTRATION_MAIL_SUBJECT);
        message.setText(String.format(REGISTRATION_SUCCESS_MESSAGE, username));

        //message.setFrom("df71968813-b3624f@inbox.mailtrap.io");
        message.setFrom("stelanz@abv.bg");
        message.setTo(email);

        this.javaMailSender.send(message);
    }
}
