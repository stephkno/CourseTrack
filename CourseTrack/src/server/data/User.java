package server.data;

import client.UserType;
import java.io.Serializable;
import server.ServerConnection;

public class User implements Serializable {

    UserType type;
    int id;
    String name;
    String password;
    public ServerConnection socket;

    public User(String name, String password, UserType type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public static boolean ValidatePassword(String password) {
        return password.length() > 0 || password.length() <= 32;
    }

    public void UpdatePassword(String newPassword) {
        password = newPassword;
    }
        
    public boolean Authenticate(String password) {
        return password.equals(this.password);
    }
    
    public int getID() {
        return id;
    }

    public UserType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}