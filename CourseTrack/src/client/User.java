package client;
import java.io.Serializable;

public class User implements Serializable {
    private final UserType userType;

    public User(UserType userType) {
        this.userType = userType;
    }
}
