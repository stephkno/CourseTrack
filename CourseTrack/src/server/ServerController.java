package server;

import client.UserType;
import client.requests.*;
import client.responses.*;

import global.*;
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
            case MessageType.PingRequest:{
                HandlePing((Message<PingRequest>) msg, client);
            }
            case MessageType.USER_REGISTER:{
                HandleRegister((Message<RegisterRequest>) msg, client);
            }
            case MessageType.USER_CHANGE_PASSWORD:{
                HandlePasswordChange((Message<PasswordChangeRequest>)msg, client);
            }
            case MessageType.USER_LOGIN:{
                HandleLogin((Message<LoginRequest>) msg, client);
            }
            default:{

            }
        }

    }

    private void HandlePing(Message<PingRequest> msg, ServerConnection client) {
        Log.Msg("PingRequest: " + msg.toString());
    }

    private void HandlePasswordChange(Message<PasswordChangeRequest> msg, ServerConnection client) {

        if(!client.IsLoggedIn()) {
            // return failure message
            client.Send(new Message<PasswordChangeResponse>(MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse("") }));
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
                client.Send(new Message<PasswordChangeResponse>(MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse[] { new PasswordChangeResponse(password) }));
            }

            // update user password in user of this session
            client.GetUser().UpdatePassword(password);
            client.Send(new Message<PasswordChangeResponse>(MessageType.USER_CHANGE_PASSWORD, MessageStatus.SUCCESS, new PasswordChangeResponse[] { new PasswordChangeResponse(password) }));

        }

    }
    
    private void HandleRegister(Message<RegisterRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.IsLoggedIn()){
            // send failure response message
            client.Send(new Message<RegisterResponse>(MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse() }));
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
                client.Send(new Message<RegisterResponse>(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse() }));
            }

            // validate password
            if(!User.ValidatePassword(password)){
                // respond with password error

            }

            // create new user object
            User newUser = new User(username, password, type);
            users.Put(username, newUser);

            // send register success message
            client.Send(new Message<RegisterResponse>(MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse[] { new RegisterResponse() }));


        }

    }

    private void HandleLogin(Message<LoginRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.IsLoggedIn()){
            // send fail response

            return;
        }
        
        ServerController controller = ServerController.Get();
        
        for(LoginRequest request : msg.getArguments()) {
            String username = request.username();
            String password = request.password();

            if(controller.HasUser(username)) {
                User user = controller.GetUser(username);

                client.User clientUser = new client.User(user.GetType());

                if(user.Authenticate(password)) {

                    Log.Msg("Login successful for user: " + username);
                    client.Send(new Message<LoginResponse>(MessageType.LOGIN_SUCCESS, MessageStatus.SUCCESS, new LoginResponse[] { new LoginResponse(clientUser) }));
                
                } else {
                
                    Log.Err("Wrong password for user: " + username);
                    client.Send(new Message<LoginResponse>(MessageType.LOGIN_FAILURE, MessageStatus.FAILURE, null));
                
                }
            } else {
                Log.Err("User not found: " + username);
                client.Send(new Message<>(MessageType.LOGIN_FAILURE, MessageStatus.FAILURE, null));
            }
        }

    }

    public void LoadData(String filepath) {

    }

}
