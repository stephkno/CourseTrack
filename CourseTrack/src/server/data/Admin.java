package server.data;
import client.UserType;

public class Admin extends User {

    public Admin(String name, String password, String employeeId) {
        super(name, password, UserType.ADMIN);
    }

}
