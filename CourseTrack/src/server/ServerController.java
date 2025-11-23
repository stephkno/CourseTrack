package server;

import client.UserType;
import global.*;
import global.data.*;
import global.requests.*;
import global.responses.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import server.data.*;

// facade class to implement all client-server event interactions
public class ServerController {

    // singleton
    private ServerController() {}
    static ServerController instance = new ServerController();
    public static ServerController Get() {
        return instance;
    }

    public void handleMessage(Message<?> msg, ServerConnection client) {

        Log.Msg("Got message: " + msg.toString());
        if(!client.isLoggedIn()){
            switch(msg.getType()) {
                case USER_LOGIN:{
                    handleLogin((Message<LoginRequest>) msg, client);
                    return;
                }
                case USER_REGISTER:{
                    handleRegister((Message<RegisterRequest>) msg, client);
                    return;
                }
            }
            // send response
            client.sendMessage(MessageType.ERROR, MessageStatus.FAILURE, new ErrorResponse[] { new ErrorResponse("User is already logged in.") });
            return;
        }

        switch(msg.getType()) {
            case PING_REQUEST:{
                handlePing((Message<PingRequest>) msg, client);
                break;
            }
            case USER_CHANGE_PASSWORD:{
                handlePasswordChange((Message<PasswordChangeRequest>)msg, client);
                break;
            }
            case ADMIN_ADD_CAMPUS:{
                handleAdminAddCampus((Message<AddCampusRequest>) msg, client);
                break;
            }
            case ADMIN_ADD_DEPARTMENT:{
                handleAdminAddDepartment((Message<AddDepartmentRequest>) msg, client);
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
            client.sendMessage( MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("User currently logged in") } );
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
            if(User.exists(username)) {
                // respond with error: username already exists
                Log.Err("handleRegister: Username already exists");
                client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Username exists") });
                continue;
            }

            // validate password
            if(!User.ValidatePassword(password)) {
                // respond with password error
                Log.Err("handleRegister: Invalid password selected");
                client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse[] { new RegisterResponse("Invalid password") });
                continue;
            }

            // create new user object
            User.add(username, password, type);

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
        
        for(LoginRequest request : msg.getArguments()) {
            String username = request.username();
            String password = request.password();

            if(!User.exists(username)) {
                Log.Err("User not found: " + username);
                client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
                continue;
            }
        
            User user = User.get(username);
            user.socket = client;
            client.setUser(user);

            client.User clientUser = new client.User(user.getType());

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
            String campusName = request.campus();

            if(Campus.exists(campusName)) {
                client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.FAILURE, new AddCampusResponse[] { new AddCampusResponse() });
                continue;
            }

            // add new campus
            Campus.add(campusName);

            // return success
            client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.SUCCESS, new AddCampusResponse[] { new AddCampusResponse() });

        }

    }

    private void handleAdminAddCourse(Message<AddCourseRequest> msg, ServerConnection client) {

        client.validateAdmin();

        for(AddCourseRequest request : msg.getArguments()) {
         
            String courseName = request.name();
            
            int number = request.number();
            int units = request.units();

            Campus campus = Campus.get(request.campus());
            Log.Msg("Addr: " + campus);

            if(campus == null) {
                Log.Err("Campus not found");
                client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse[] { new AddCourseResponse(null) } );
                continue;
            }

            Department department = campus.getDepartment(request.department());
            if(department == null){
                Log.Err("Department not found");
                client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse[] { new AddCourseResponse(null) } );
                continue;
            }

            Course newCourse = new Course(courseName, number, units, department);
            if(!department.addCourse(newCourse)){
                Log.Err("Course exists in department");
                client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse[] { new AddCourseResponse(null) } );
                continue;
            }

            // send success response
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.SUCCESS, new AddCourseResponse[] { new AddCourseResponse(newCourse) } );

        }

    }

    private void handleAdminAddDepartment(Message<AddDepartmentRequest> msg, ServerConnection client) {

        client.validateAdmin();

        for(AddDepartmentRequest request : msg.getArguments()) {
         
            String departmentName = request.name();
            Campus campus = Campus.get(request.campus());
            Log.Msg("Addr: " + campus);

            if(campus == null) {
                Log.Err("Campus not found");
                client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.FAILURE, new AddDepartmentResponse[] { new AddDepartmentResponse("") } );
                continue;
            }

            // check if contains department
            if(campus.hasDepartment(departmentName)){
                Log.Err("Campus already contains department");
                client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.FAILURE, new AddDepartmentResponse[] { new AddDepartmentResponse("") } );
                continue;
            }
            
            campus.addDepartment(departmentName);
            Log.Msg("Campus added department");

            // send success response
            client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.SUCCESS, new AddDepartmentResponse[] { new AddDepartmentResponse("") } );

        }

    }

    private void handleAdminAddSection(Message<AddSectionRequest> msg, ServerConnection client) {

        client.validateAdmin();

        for(AddSectionRequest request : msg.getArguments()) {
         
            String courseName = request.name();

            Campus campus = Campus.get(request.campus());
            if(campus == null) {
                // return failure response
                client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse[] { new AddSectionResponse("") } );
                continue;
            }

            Department department = campus.getDepartment(request.department());

            // need to get course by name? id?
            Course course = department.getCourse(request.courseId());
            Term term = Term.get(request.term());

            Section newSection = new Section(0, request.capacity(), course, request.term(), campus);
            
            if(!term.addSection(newSection)){
                // return error response
                client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse[] { new AddSectionResponse("") } );
            }

            // send success response
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.SUCCESS, new AddSectionResponse[] { new AddSectionResponse("") } );

        }

    }

    public static void Serialize(String filepath, boolean save) {

        try {
            
            if(save) {
                FileOutputStream fileStream = new FileOutputStream(filepath);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                User.save(objectStream);
                Term.save(objectStream);
                Campus.save(objectStream);
            } else {
                FileInputStream fileStream = new FileInputStream(filepath);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                User.load(objectStream);
                Term.load(objectStream);
                Campus.load(objectStream);
            }

        } catch (IOException e) {
        
            e.printStackTrace();
        
        
        }
    }

}
