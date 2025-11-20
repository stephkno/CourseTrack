package server;

import client.UserType;
import client.requests.*;
import client.responses.*;
import global.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.tree.DefaultMutableTreeNode;
import server.data.*;

// facade class to implement all client-server event interactions
public class ServerController {

    // singleton
    private ServerController() {}
    static ServerController instance = new ServerController();
    public static ServerController Get() {
                   
        return instance;
    }

    HashMap<User> users = new HashMap<>();
    HashMap<Campus> campuses = new HashMap<>();
    LinkedList<Term> terms = new LinkedList<>();

    public User GetUser(String username) {
        return users.Get(username);
    }

    public boolean HasUser(String username) {
        return users.Contains(username);
    }

    private boolean AuthorizeUser(User user, UserType type){
        return user.GetType() == type;
    }

    public void HandleMessage(Message<?> msg, ServerConnection client) {

        Log.Msg("Got message: " + msg.toString());
        switch(msg.getType()) {
            case PING_REQUEST:{
                HandlePing((Message<PingRequest>) msg, client);
                break;
            }
            case USER_REGISTER:{
                HandleRegister((Message<RegisterRequest>) msg, client);
                break;
            }
            case USER_CHANGE_PASSWORD:{
                HandlePasswordChange((Message<PasswordChangeRequest>)msg, client);
                break;
            }
            case USER_LOGIN:{
                HandleLogin((Message<LoginRequest>) msg, client);
                break;
            }
            case ADMIN_ADD_CAMPUS:{
                HandleAdminAddCampus((Message<AddCampusRequest>) msg, client);
                break;
            }
            case ADMIN_ADD_COURSE:{
              //  HandleAdminAddCourse((Message<AddCampusRequest>) msg, client);
            }
            default:{
                break;
            }
        }

    }

    private void HandlePing(Message<PingRequest> msg, ServerConnection client) {
        Log.Msg("PING_REQUEST: " + msg.toString());
        client.SendMessage( MessageType.PING_REQUEST, MessageStatus.RESPONSE, new PingResponse[] { new PingResponse("pong") } );
    }

    private void HandlePasswordChange(Message<PasswordChangeRequest> msg, ServerConnection client) {

        if(!client.IsLoggedIn()) {
            // return failure message
            client.SendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );
            return;
        }

        // for each request
        PasswordChangeRequest[] requests = msg.getArguments();
        
        for(PasswordChangeRequest request : requests){

            // get data
            String password = request.password();

            // validate password
            if(!User.ValidatePassword(password)){
                // respond with password error message
                client.SendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );
                continue;
            }

            // update user password in user of this session
            client.GetUser().UpdatePassword(password);
            client.SendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.SUCCESS, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );

        }

    }
    
    private void HandleRegister(Message<RegisterRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.IsLoggedIn()){
            // send failure response message
            client.SendMessage( MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse("User currently logged in") } );
            return;
        }

        // for each request
        RegisterRequest[] requests = msg.getArguments();
        
        for(RegisterRequest request : requests){
            // get data
            String username = request.username();
            String password = request.password();
            UserType type = request.type();

            // validate username
            if(users.Contains(username)){
                // respond with error: username already exists
                client.SendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Username exists") });
                continue;
            }

            // validate password
            if(!User.ValidatePassword(password)){
                // respond with password error
                client.SendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Invalid password") });
                continue;
            }

            // create new user object
            User newUser = new User(username, password, type);
            users.Put(username, newUser);
            DefaultMutableTreeNode newUserNode = new DefaultMutableTreeNode(newUser.GetName());

            // send register success message
            client.SendMessage(MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse("") });

        }

    }

    private void HandleLogin(Message<LoginRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.IsLoggedIn()){
            // send fail response
            client.SendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, new LoginResponse[] { new LoginResponse(null) });
            return;
        }
        
        ServerController controller = ServerController.Get();
        
        for(LoginRequest request : msg.getArguments()) {
            String username = request.username();
            String password = request.password();

            if(!controller.HasUser(username)) {
                Log.Err("User not found: " + username);
                client.SendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
                continue;
            }
        
            User user = controller.GetUser(username);
            client.SetUser(user);
            client.User clientUser = new client.User(user.GetType());

            if(!user.Authenticate(password)) {

                Log.Err("Wrong password for user: " + username);
                client.SendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
                continue;
            
            }

            Log.Msg("Login successful for user: " + username);
            client.SendMessage(MessageType.USER_LOGIN, MessageStatus.SUCCESS, new LoginResponse[] { new LoginResponse(clientUser) });
            
        }

    }

    private void HandleAdminAddCampus(Message<AddCampusRequest> msg, ServerConnection client) {
        
        client.ValidateAdmin();

        for(AddCampusRequest request : msg.getArguments()) {
            String campusName = request.campusName();

            // validate campus name
            if(campuses.Contains(campusName)) {
                // send error response
                client.SendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.FAILURE, new AddCampusResponse[] { new AddCampusResponse("Campus already exists!") });
                continue;
            }

            Campus newCampus = new Campus(campusName);
            campuses.Put(campusName, newCampus);
            DefaultMutableTreeNode newCampusNode = new DefaultMutableTreeNode(newCampus.getCampusName());

            // return success
            client.SendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.SUCCESS, new AddCampusResponse[] { new AddCampusResponse("") });

        }

    }

    private void HandleAdminAddCourse(Message<AddCourseRequest> msg, ServerConnection client) {

        client.ValidateAdmin();

        for(AddCourseRequest request : msg.getArguments()) {
         
            String courseName = request.name();
            int units = request.units();
            Department department = request.department();
            Term term = request.term();

            // check if term exists in server
                        

        }

    }

    public void Serialize(String filepath, boolean save) {

        try {
            
            if(save) {
                FileOutputStream fileStream = new FileOutputStream(filepath);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeObject(this);
            } else {
                FileInputStream fileStream = new FileInputStream(filepath);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                //this = objectStream.readObject();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
