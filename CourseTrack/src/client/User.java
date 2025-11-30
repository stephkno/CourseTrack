package client;
import java.io.Serializable;

public class User implements Serializable {

    private final UserType userType;
    private String userName;

    public User(String userName, UserType userType) {
        this.userName = userName;
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

}
