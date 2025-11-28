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

/*
todo

admin can delete everything
student waitlist
student class schedule
student generate schedules

term semester vs quarter type?

notifications

reports

*/

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

        // lobby requests
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
            client.sendMessage(MessageType.ERROR, MessageStatus.FAILURE, new ErrorResponse("User is already logged in.") );
            return;
        }

        client.getUser().setActive();

        switch(msg.getType()) {

            case PING_REQUEST:{
                handlePing((Message<PingRequest>) msg, client);
                break;
            }

            // user requests
            case USER_LOGOUT:{
                handleLogout((Message<LogoutRequest>) msg, client);
                break;
            }
            case USER_CHANGE_PASSWORD:{
                handlePasswordChange((Message<PasswordChangeRequest>)msg, client);
                break;
            }
            
            // admin requests
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
                break;
            }
            case ADMIN_ADD_SECTION:{
                handleAdminAddSection((Message<AddSectionRequest>) msg, client);
                break;
            }

            // student requests
            case STUDENT_BROWSE_SECTION:{
                handleStudentBrowseSection((Message<BrowseSectionRequest>) msg, client);
                break;
            }
            case STUDENT_ENROLL:{
                handleStudentEnrollSection((Message<EnrollSectionRequest>) msg, client);
                break;
            }
            case STUDENT_DROP:{
                handleStudentDropSection((Message<DropSectionRequest>) msg, client);
                break;
            }
            case STUDENT_GET_SCHEDULE:{
                handleStudentGetSchedule((Message<GetScheduleRequest>) msg, client);
                break;
            }
            default:{
                break;
            }
        }

    }

    private void handlePing(Message<PingRequest> msg, ServerConnection client) {
        Log.Msg("PING_REQUEST: " + msg.toString());
        client.sendMessage( MessageType.PING_REQUEST, MessageStatus.RESPONSE, new PingResponse("pong") );
    }

    private void handlePasswordChange(Message<PasswordChangeRequest> msg, ServerConnection client) {

        if(!client.isLoggedIn()) {
            // return failure message
            client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse("") );
            return;
        }

        PasswordChangeRequest request = msg.get();

        // get data
        String password = request.password();

        // validate password
        if(!User.validatePassword(password)) {
            // respond with password error message
            client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.FAILURE, new PasswordChangeResponse("") );
            return;
        }

        // update user password in user of this session
        client.getUser().updatePassword(password);
        client.sendMessage( MessageType.USER_CHANGE_PASSWORD, MessageStatus.SUCCESS,new PasswordChangeResponse("") );


    }

    private void handleRegister(Message<RegisterRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.isLoggedIn()) {
            // send failure response message
            client.sendMessage( MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse("User currently logged in") );
            return;
        }

        RegisterRequest request = msg.get();
        
        // get data
        String username = request.username();
        String password = request.password();
        UserType type = request.type();

        // validate username
        if(User.exists(username)) {
            // respond with error: username already exists
            Log.Err("handleRegister: Username already exists");
            client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse("Username exists") );
            return;
        }

        // validate password
        if(!User.validatePassword(password)) {
            // respond with password error
            Log.Err("handleRegister: Invalid password selected");
            client.sendMessage(MessageType.USER_REGISTER, MessageStatus.FAILURE, new RegisterResponse("Invalid password") );
            return;
        }

        // create new user object
        User.add(username, password, type);

        // send register success message
        client.sendMessage(MessageType.USER_REGISTER, MessageStatus.SUCCESS,new RegisterResponse("") );

    }

    private void handleLogin(Message<LoginRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.isLoggedIn()) {
            // send fail response
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, new LoginResponse(null) );
            return;
        }

        LoginRequest request = msg.get();
        String username = request.username();
        String password = request.password();

        if(!User.exists(username)) {
            Log.Err("User not found: " + username);
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
            return;
        }
    
        User user = User.get(username);
        user.socket = client;
        user.login();
        client.setUser(user);

        client.User clientUser = new client.User(user.getType());

        if(!user.authenticate(password)) {

            Log.Err("Wrong password for user: " + username);
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
            return;
        
        }

        Log.Msg("Login successful for user: " + username);
        client.sendMessage(MessageType.USER_LOGIN, MessageStatus.SUCCESS, new LoginResponse(clientUser) );
        
    }

    private void handleAdminAddCampus(Message<AddCampusRequest> msg, ServerConnection client) {
        
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.FAILURE, new AddCampusResponse(null));
            return;
        }
        AddCampusRequest request = msg.get();
        String campusName = request.campus();

        if(Campus.exists(campusName)) {
            client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.FAILURE, new AddCampusResponse(null) );
            return;
        }

        // add new campus
        Campus campus = Campus.add(campusName);

        // return success
        client.sendMessage(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.SUCCESS, new AddCampusResponse(campus) );

    }

    private void handleAdminAddCourse(Message<AddCourseRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse(null));
            return;
        }

        AddCourseRequest request = msg.get();
        
        String courseName = request.name();
        
        int number = request.number();
        int units = request.units();

        Campus campus = Campus.get(request.campus());
        Log.Msg("Addr: " + campus);

        if(campus == null) {
            Log.Err("Campus not found");
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse(null) );
            return;
        }

        Department department = campus.getDepartment(request.department());
        if(department == null){
            Log.Err("Department not found");
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse(null) );
            return;
        }

        Course newCourse = new Course(courseName, number, units, department);
        if(!department.addCourse(newCourse)){
            Log.Err("Course exists in department");
            client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.FAILURE, new AddCourseResponse(null) );
            return;
        }

        // send success response
        client.sendMessage(MessageType.ADMIN_ADD_COURSE, MessageStatus.SUCCESS, new AddCourseResponse(newCourse) );

    }

    private void handleAdminAddDepartment(Message<AddDepartmentRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.FAILURE, new AddDepartmentResponse(null));
            return;
        }

        AddDepartmentRequest request = msg.get();
        
        String departmentName = request.name();
        Campus campus = Campus.get(request.campus());
        Log.Msg("Addr: " + campus);

        if(campus == null) {
            Log.Err("Campus not found");
            client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.FAILURE, new AddDepartmentResponse("") );
            return;
        }

        // check if contains department
        if(campus.hasDepartment(departmentName)){
            Log.Err("Campus already contains department");
            client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.FAILURE, new AddDepartmentResponse("") );
            return;
        }
        
        campus.addDepartment(departmentName);
        Log.Msg("Campus added department");

        // send success response
        client.sendMessage(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.SUCCESS, new AddDepartmentResponse("") );

    }

    private void handleAdminAddSection(Message<AddSectionRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse(null));
            return;
        }

        AddSectionRequest request = msg.get();
        
        Campus campus = Campus.get(request.campus());
        if(campus == null) {
            // return failure response
            Log.Err("Campus not found");
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse(null) );
            return;
        }

        if(!campus.hasDepartment(request.department())){
            Log.Err("Department not found");
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse(null) );
            return;
        }

        Department department = campus.getDepartment(request.department());

        // need to get course by name? id?
        Log.Msg(request.courseId());

        Course course = department.getCourse(request.courseId());
        Term term = Term.get(request.term());

        Section newSection = new Section(request.capacity(), course, request.term(), department, request.instructor(), request.meetTimes());
        
        if(!term.addSection(newSection)){
            // return error response
            Log.Err("Section already exists");
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse(null)  );
            return;
        }

        // send success response
        client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.SUCCESS, new AddSectionResponse(newSection)  );

    }
    
    private void handleStudentBrowseSection(Message<BrowseSectionRequest> msg, ServerConnection client) {

        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.FAILURE, new BrowseSectionResponse( new Section[]{} ));
            return;
        }

        BrowseSectionRequest request = msg.get();
        
        String query = request.searchQuery();
        Campus campus = Campus.get(request.campus());
        Department department = campus.getDepartment(request.department());
        Term term = Term.get(request.term());

        // must use array because no way to return a generic array from linked list
        Section[] searchResults = new Section[32];

        if(term == null || campus == null || department == null || query.equals("")){
            client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.SUCCESS, new BrowseSectionResponse( new Section[]{} ) );
            return;
        }

        int i = 0;

        // linear search for relevant sections
        // search by class name 
        LinkedList<Section> sections = term.getSections();
        Log.Msg(sections.Length());

        for(Section section : sections){

            if(i > 32){
                break;
            } 
            if(!section.getDepartment().equals(department) || !section.getDepartment().getCampus().equals(campus)){
                continue;
            }

            if(section.Search(query)){
                Log.Msg("Adding section to search results!" + section);
                searchResults[i++] = section;
                continue;
            } 

        }

        BrowseSectionResponse res = new BrowseSectionResponse(searchResults);
        client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.SUCCESS, res);

    }

    private void handleStudentEnrollSection(Message<EnrollSectionRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0));
            return;
        }

        EnrollSectionRequest request = msg.get();
        
        int sectionId = request.sectionId();
        Term term = Term.get(request.term());
        Student student = (Student)client.getUser();
        
        Section section = term.getSection(sectionId);
        
        // validate enrollment requirements
        if(!section.getCourse().verifyPrereqs(student)){
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0));
            return;
        }
        
        // validate that student not already registered for this section!
        // wait is this necessary? schedule conflict should catch this.
        //if(student.isEnrolled(section)){
        //    client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null, 0));
        //}

        // validate schedule conflicts
        for(Section other : student.getEnrolledSections()){

            // iterate all meet times of currently enrolled classes
            if(section.conflicts(other.getMeetTimes())){
                client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0));
                return;
            }
    
        }

        // validate waitlist
        if(section.full()){
            
            // send waitlist response
            int waitlist_position = section.addWaitlist(student);
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null, waitlist_position));
            return;

        }

        // add student to section
        section.addStudent(student);
        student.addSection(section);

        EnrollSectionResponse res = new EnrollSectionResponse(section, 0);
        client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.SUCCESS, res);

    }

    private void handleStudentDropSection(Message<DropSectionRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_DROP, MessageStatus.FAILURE, new DropSectionResponse());
            return;
        }
        
        DropSectionRequest request = msg.get();
        
        int sectionId = request.sectionId();
        Term term = Term.get(request.term());
        Student student = (Student)client.getUser();
        Section section = term.getSection(sectionId);
        
        section.removeStudent(student);
        student.removeSection(section);

        // waitlist logic
        if(section.waitlistLength() > 0){

            Student studentOnWaitlist = section.popWaitlist();
            
            section.addStudent(studentOnWaitlist);
            studentOnWaitlist.addSection(section);

            studentOnWaitlist.Notify("You have been enrolled!");

            // iterate students in wait list and notify them of waitlist promotion?
            for(Student s : section.getWaitlist()){
                s.Notify("Your waitlist position has changed.");
            }
       
        }

        DropSectionResponse res = new DropSectionResponse();
        client.sendMessage(MessageType.STUDENT_DROP, MessageStatus.SUCCESS, res);

    }

    private void handleStudentGetSchedule(Message<GetScheduleRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.FAILURE, new GetScheduleResponse( new Section[] { } ));
            return;
        }

        // get currently enrolled sections s
        Student student = (Student)client.getUser();
        int n = student.getEnrolledSections().Length();

        Section[] sections = new Section[n];

        if(student != null){
            int i = 0;
            for(Section s : student.getEnrolledSections()){
                sections[i++] = s;
            }
        }

        Log.Msg("Sending schedule response");

        GetScheduleResponse res = new GetScheduleResponse(sections);
        client.sendMessage(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.SUCCESS, res);

    }

    private void handleLogout(Message<LogoutRequest> msg, ServerConnection client) {

        // send success response
        client.sendMessage(MessageType.USER_LOGOUT, MessageStatus.SUCCESS, new LogoutResponse[] { new LogoutResponse() } );
        
        // clear session
        client.setUser(null);

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
