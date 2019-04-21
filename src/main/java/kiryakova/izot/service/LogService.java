package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.LogServiceModel;
import org.springframework.messaging.Message;

import java.util.List;

public interface LogService {

    void addEvent(Message<String> event);

    List<LogServiceModel> findAllLogsByDateTimeDesc();

    void logAction(String username, String event);
}
