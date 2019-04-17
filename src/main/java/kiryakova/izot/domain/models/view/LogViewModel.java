package kiryakova.izot.domain.models.view;

import java.time.LocalDateTime;

public class LogViewModel {

    private String event;
    private LocalDateTime dateTime;
    private UserViewModel user;

    public LogViewModel() {
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public UserViewModel getUser() {
        return this.user;
    }

    public void setUser(UserViewModel user) {
        this.user = user;
    }

}
