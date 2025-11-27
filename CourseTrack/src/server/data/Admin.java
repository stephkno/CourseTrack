package server.data;
import client.UserType;
import java.io.Serializable;

public class Admin extends User implements Serializable {

    int id;
    static int nextId = 0;

    public Admin(String name, String password) {
        super(name, password, UserType.ADMIN);
        this.id = nextId++;
    }

}
