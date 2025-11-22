package server;

import client.UserType;
import global.*;
import global.requests.*;
import global.responses.*;
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

    public HashMap<User> getUsers() {
        return users;
    }

    public HashMap<Campus> getCampuses() {
        return campuses;
    }
    
    public Campus getCampus(String campusName) {
        return campuses.Get(campusName);
    }

    public LinkedList<Term> getTerms() {
        return terms;
    }

    public User getUser(String username) {
        return users.Get(username);
    }

    public boolean hasUser(String username) {
        return users.Contains(username);
    }

    private boolean authorizeUser(User user, UserType type) {
        return user.getType() == type;
    }

    public void handleMessage(Message<?> msg, ServerConnection client) {

        Log.Msg("Got message: " + msg.toString());
        switch(msg.getType()) {
            case PING_REQUEST:{
                handlePing((Message<PingRequest>) msg, client);
                break;
            }
            case USER_REGISTER:{
                handleRegister((Message<RegisterRequest>) msg, client);
                break;
            }
            case USER_CHANGE_PASSWORD:{
                handlePasswordChange((Message<PasswordChangeRequest>)msg, client);
                break;
            }
            case USER_LOGIN:{
                handleLogin((Message<LoginRequest>) msg, client);
                break;
            }
            case ADMIN_ADD_CAMPUS:{
                handleAdminAddCampus((Message<AddCampusRequest>) msg, client);
                break;
            }
            case ADMIN_ADD_COURSE:{
                handleAdminAddCourse((Message<AddCourseRequest>) msg, client);
            }
            default:{
                break;
            }
        }

    }

    private void handlePing(Message<PingRequest> msg, ServerConnection client) {
        Log.Msg("PING_REQUEST: " + msg.toString());
        client.sendMessage( MessageType.PING_REQUEST, MessageStatus.RESPONSE, new PingResponse[] { new PingResponse("pong") } );
    }

    private void handlePasswordChange(Message<PasswordChangeRequest> msg, ServerConnection client) {

        if(!client.isLoggedIn()) {
            // return failure message
            client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );
            return;
        }

        // for each request
        PasswordChangeRequest[] requests = msg.getArguments();
        
        for(PasswordChangeRequest request : requests) {

            // get data
            String password = request.password();

            // validate password
            if(!User.ValidatePassword(password)) {
                // respond with password error message
                client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );
                continue;
            }

            // update user password in user of this session
            client.getUser().UpdatePassword(password);
            client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.SUCCESS, new PasswordChangeResponse[] { new PasswordChangeResponse("") } );

        }

    }
    
    private void handleRegister(Message<RegisterRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.isLoggedIn()) {
            // send failure response message
            client.sendMessage( MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse("User currently logged in") } );
            return;
        }

        // for each request
        RegisterRequest[] requests = msg.getArguments();
        
        for(RegisterRequest request : requests) {
            // get data
            String username = request.username();
            String password = request.password();
            UserType type = request.type();

            // validate username
            if(users.Contains(username)) {
                // respond with error: username already exists
                client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Username exists") });
                continue;
            }

            // validate password
            if(!User.ValidatePassword(password)) {
                // respond with password error
                client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Invalid password") });
                continue;
            }

            // create new user object
            User newUser = new User(username, password, type);
            users.Put(username, newUser);
            DefaultMutableTreeNode newUserNode = new DefaultMutableTreeNode(newUser.getName());

            // send register success message
            client.sendMessage(MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse("") });

        }

    }

    private void handleLogin(Message<LoginRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.isLoggedIn()) {
            // send fail response
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, new LoginResponse[] { new LoginResponse(null) });
            return;
        }
        
        ServerController controller = ServerController.Get();
        
        for(LoginRequest request : msg.getArguments()) {
            String username = request.username();
            String password = request.password();

            if(!controller.hasUser(username)) {
                Log.Err("User not found: " + username);
                client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
                continue;
            }
        
            User user = controller.getUser(username);
            client.setUser(user);
            client.User clientUser = new client.User(user.getType());
            user.socket = client;

            if(!user.Authenticate(password)) {

                Log.Err("Wrong password for user: " + username);
                client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
                continue;
            
            }

            Log.Msg("Login successful for user: " + username);
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.SUCCESS, new LoginResponse[] { new LoginResponse(clientUser) });
            
        }

    }

    private void handleAdminAddCampus(Message<AddCampusRequest> msg, ServerConnection client) {
        
        client.validateAdmin();

        for(AddCampusRequest request : msg.getArguments()) {
            String campusName = request.campusName();

            // validate campus name
            if(campuses.Contains(campusName)) {
                // send error response
                client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.FAILURE, new AddCampusResponse[] { new AddCampusResponse(null) });
                continue;
            }

            Campus newCampus = new Campus(campusName);
            campuses.Put(campusName, newCampus);

            // return success
            client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.SUCCESS, new AddCampusResponse[] { new AddCampusResponse(newCampus) });

        }

    }

    private void handleAdminAddCourse(Message<AddCourseRequest> msg, ServerConnection client) {

        client.validateAdmin();

        for(AddCourseRequest request : msg.getArguments()) {
         
            String courseName = request.name();
            
            int number = request.number();
            int units = request.units();

            Campus campus = ServerController.Get().getCampus(request.campusName());
            if(campus == null) {
                // return failure response
                client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse[] { new AddCourseResponse(null) } );
                continue;
            }

            Department department = campus.getDepartment(request.departmentName());

            Course newCourse = new Course(courseName, number, units, department);
            
            if(!campus.AddCourse(newCourse)){
                // return error response
                client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse[] { new AddCourseResponse(null) } );
            }

            // send success response
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.SUCCESS, new AddCourseResponse[] { new AddCourseResponse(newCourse) } );

        }

    }

    public static void Serialize(String filepath, boolean save) {

        try {
            
            if(save) {
                FileOutputStream fileStream = new FileOutputStream(filepath);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeObject(instance);
            } else {
                FileInputStream fileStream = new FileInputStream(filepath);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                instance = (ServerController)objectStream.readObject();
            }

        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
