package server.data;

import client.UserType;
import server.ServerConnection;

public class User {

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
    
    public int GetID() {
        return id;
    }

    public UserType GetType() {
        return type;
    }

    public String GetName() {
        return name;
    }

}