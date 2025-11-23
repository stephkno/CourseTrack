package server.data;

import client.UserType;
import global.HashMap;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import server.ServerConnection;

public class User implements Serializable {

    UserType type;
    int id;
    String name;
    String password;
    public ServerConnection socket;

    public static HashMap<User> users = new HashMap<>();
    
    public static HashMap<User> get(){
        return users;
    }

    public static User get(String username) {
        return User.users.Get(username);
    }

    public static User add(String username, String password, UserType type) {
        User newUser = new User(username, password, type);
        users.Put(username, newUser);
        return newUser;
    }

    public static boolean exists(String username) {
        return users.Contains(username);
    }

    public static void save(ObjectOutputStream objectStream) {
        try {
            objectStream.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void load(ObjectInputStream objectStream) {
        try {
            users = (HashMap<User>)objectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected User(String name, String password, UserType type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }

    private boolean authorize(UserType type) {
        return getType() == type;
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

    public String toString(){
        String outstring = "";
        outstring += id + ": " + name + " " + type + " ";
        if(socket.isLoggedIn()){
            outstring += " [Logged in]";
        }else{
            outstring += " [Not logged in]";
        }
        return outstring;
    }

}