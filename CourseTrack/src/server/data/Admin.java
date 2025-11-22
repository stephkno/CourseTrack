package server.data;
import client.UserType;
import java.io.Serializable;

public class Admin extends User implements Serializable {

    public Admin(String name, String password, String employeeId) {
        super(name, password, UserType.ADMIN);
    }

}
