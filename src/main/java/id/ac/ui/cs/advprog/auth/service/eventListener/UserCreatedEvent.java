package id.ac.ui.cs.advprog.auth.service.eventListener;

import id.ac.ui.cs.advprog.auth.model.auth.User;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {

    private User user;
    private String jwtToken;

    public UserCreatedEvent(User user, String jwtToken) {
        super(user);
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
