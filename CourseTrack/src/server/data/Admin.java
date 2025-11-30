package server.data;
import client.UserType;
import java.io.Serializable;
import global.HashMap;
import global.data.Campus;

public class Admin extends User implements Serializable {

    int id;
    static int nextId = 0;

    HashMap<String, Campus> campuses;

    public Admin(String name, String password) {
        super(name, password, UserType.ADMIN);
        this.id = nextId++;
    }

}
