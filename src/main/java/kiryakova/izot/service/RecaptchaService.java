package kiryakova.izot.service;

public interface RecaptchaService {
    String verifyRecaptcha(String userIpAddress, String gRecaptchaResponse);
}
