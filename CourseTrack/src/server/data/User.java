package server.data;

import client.UserType;
import global.HashMap;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import server.ServerConnection;
import java.time.LocalDateTime;
import global.Message;

public class User implements Serializable {

    UserType type;
    int id;
    String name;
    String password;

    LocalDateTime lastLogin;
    LocalDateTime lastActivity;
    
    public ServerConnection socket;

    public void Notify(String message){
        // push notification to notifications list
        // socket.Send(new Message(MessageType.STUDENT_WAS_ENROLLED, MessageStatus.REQUEST, new (message)));
    }

    public static HashMap<String, User> users = new HashMap<>();
    
    public static HashMap<String, User> get(){
        return users;
    }

    public static User get(String username) {
        return User.users.Get(username);
    }

    public static User add(String username, String password, UserType type) {
        User newUser;

        if(type == UserType.STUDENT){
            newUser = (User)new Student(username, password);
        }else{
            newUser = (User)new Admin(username, password);
        }

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
            users = (HashMap<String, User>)objectStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected User(String name, String password, UserType type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public void login(){
        lastLogin = LocalDateTime.now();
    }

    public void setActive(){
        lastActivity = LocalDateTime.now();
    }

    private boolean authorize(UserType type) {
        return getType() == type;
    }

    public static boolean validatePassword(String password) {
        return password.length() > 0 || password.length() <= 32;
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }
        
    public boolean authenticate(String password) {
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