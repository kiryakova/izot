package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Log;
import kiryakova.izot.domain.entities.User;
import kiryakova.izot.domain.models.service.LogServiceModel;
import kiryakova.izot.domain.models.service.UserServiceModel;
import kiryakova.izot.repository.LogRepository;
import kiryakova.izot.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserService userService;
    private final UserValidationService userValidation;
    private final JmsTemplate jmsTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, UserService userService, UserValidationService userValidation, JmsTemplate jmsTemplate, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.userValidation = userValidation;
        this.jmsTemplate = jmsTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    @JmsListener(destination = "message-queue")
    public void addEvent(Message<String> event) {
        Log log = this.createLog(event.getPayload().split(";"));

        this.logRepository.save(log);
    }

    @Override
    public List<LogServiceModel> findAllLogsByDateTimeDesc() {
        return this.logRepository.findAllLogsByDateTimeDesc()
                .stream()
                .map(log ->{
                    LogServiceModel logServiceModel = this.modelMapper.map(log, LogServiceModel.class);
                    logServiceModel.getUser().setUsername(log.getUser().getUsername());

                    return logServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void logAction(UserServiceModel userServiceModel, String event) {
        this.jmsTemplate.convertAndSend(String.format("%s;%s;%s", LocalDateTime.now(), userServiceModel.getUsername(), event));
    }

    @Override
    public void logAction(String username, String event) {
        this.jmsTemplate.convertAndSend(String.format("%s;%s;%s", LocalDateTime.now(), username, event));
    }

    private Log createLog(String[] eventParams) {
        Log log = new Log();
        log.setDateTime(this.formatDate(eventParams[0]));


        UserServiceModel userServiceModel = this.userService.findUserByUsername(eventParams[1]);

        if(!userValidation.isValid(userServiceModel)) {
            throw new IllegalArgumentException();
        }

        log.setUser(this.modelMapper.map(userServiceModel, User.class));
        log.setEvent(eventParams[2]);

        return log;
    }

    private LocalDateTime formatDate(String dateTimeStr) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime
                .parse(dateTimeStr.replace("T", " ")
                        .substring(0, dateTimeStr.lastIndexOf(".")), format);
    }

}
