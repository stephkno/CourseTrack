package server.data;

public class User {
    
    int id;
    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void ChangePassword(String newPassword) {
        password = newPassword;
    }
    
    public boolean Authenticate(String password){
        return false;
    }
    
}