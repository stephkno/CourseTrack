package server;

import client.UserType;
import global.*;
import global.data.*;
import global.requests.*;
import global.requests.AdminRequests.*;
import global.requests.StudentRequests.*;
import global.responses.*;
import global.responses.AdminResponses.*;
import global.responses.StudentResponses.*;
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

        // lobby requests
        if(!client.isLoggedIn()){
            
            switch(msg.getType()) {
                case GET_CAMPUSES:{
                    handleGetCampuses((Message<GetCampusesRequest>) msg, client);
                    return;
                }
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
            case GET_CAMPUSES:{
                handleGetCampuses((Message<GetCampusesRequest>) msg, client);
                return;
            }

            // user requests
            case USER_LOGIN:{
                handleLogin((Message<LoginRequest>) msg, client);
                return;
            }
            case USER_REGISTER:{
                handleRegister((Message<RegisterRequest>) msg, client);
                return;
            }

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
            case ADMIN_GET_REPORT:{
                handleAdminGetReport((Message<ReportRequest>) msg, client);
                break;
            }
            case ADMIN_GET_USERS:{
                handleAdminGetUsers((Message<AdminGetUsersRequest>) msg, client);
                break;
            }
            case ADMIN_GET_CAMPUSES:{
                handleAdminGetCampuses((Message<AdminGetCampusesRequest>) msg, client);
                break;
            }
            case ADMIN_GET_DEPARTMENTS:{
                handleAdminGetDepartments((Message<AdminGetDepartmentsRequest>) msg, client);
                break;
            }
            case GET_TERMS:{
                handleGetTerms((Message<GetTermsRequest>) msg, client);
                break;
            }
            case ADMIN_GET_COURSES:{
                handleAdminGetCourses((Message<AdminGetCoursesRequest>) msg, client);
                break;
            }
            case ADMIN_GET_SECTIONS:{
                handleAdminGetSections((Message<AdminGetSectionsRequest>) msg, client);
                break;
            }
            case ADMIN_REMOVE_USER:{
                handleAdminRemoveUser((Message<AdminRemoveUserRequest>) msg, client);
                break;
            }
            case ADMIN_REMOVE_CAMPUS:{
                handleAdminRemoveCampus((Message<AdminRemoveCampusRequest>) msg, client);
                break;
            }
            case ADMIN_REMOVE_DEPARTMENT:{
                handleAdminRemoveDepartment((Message<AdminRemoveDepartmentRequest>) msg, client);
                break;
            }
            case ADMIN_REMOVE_COURSE:{
                handleAdminRemoveCourse((Message<AdminRemoveCourseRequest>) msg, client);
                break;
            }
            case ADMIN_REMOVE_SECTION:{
                handleAdminRemoveSection((Message<AdminRemoveSectionRequest>) msg, client);
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
            case STUDENT_GET_UNITS:{
                handleStudentGetUnits((Message<StudentGetUnitRequest>) msg, client);
                break;
            }
            case STUDENT_GET_PROGRESS:{
                handleStudentGetProgress((Message<StudentGetProgressRequest>) msg, client);
                break;
            }
            default:{
                Log.Err("Unknown request! " + msg.toString());
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
        client.sendMessage(MessageType.USER_REGISTER, MessageStatus.SUCCESS, new RegisterResponse("") );

    }

    private void handleGetCampuses(Message<GetCampusesRequest> msg, ServerConnection client) {

        LinkedList<Campus> campuses = new LinkedList<>();

        for(Campus campus : Campus.get()){
            campuses.Push(campus);
        }

        client.sendMessage(MessageType.GET_CAMPUSES, MessageStatus.SUCCESS, new GetCampusesResponse(campuses) );
        return;
        
    }

    private void handleLogin(Message<LoginRequest> msg, ServerConnection client) {

        // user should not be logged in
        if(client.isLoggedIn()) {
            Log.Err("handleLogin error: User already logged in.");
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

        if(user == null){
            Log.Err("ERROR: User null on login");
        }else{
            Log.Msg("Login in user: " + user);
        }

        client.setUser(user);

        client.User clientUser = new client.User(user.getName(), user.getType());

        if(!user.authenticate(password)) {

            Log.Err("Wrong password for user: " + username);
            client.sendMessage(MessageType.USER_LOGIN, MessageStatus.FAILURE, null);
            return;
        
        }

        client.Send(new Message<NotificationRequest>(MessageType.NOTIFICATION, MessageStatus.REQUEST, new NotificationRequest( user.getNotifications() )));

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
        ((Admin)client.getUser()).addCampus(campus);

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

        Course newCourse = new Course(courseName, number, units, department, request.requirements());
        ((Admin)client.getUser()).addCourse(newCourse);

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
        
        Department newDepartment = campus.addDepartment(departmentName);
        ((Admin)client.getUser()).addDepartment(newDepartment);
        
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

        Section newSection = new Section(request.capacity(), course, term, department, request.instructor(), request.meetTimes());
        ((Admin)client.getUser()).addSection(newSection);
        
        if(!term.addSection(newSection)){
            // return error response
            Log.Err("Section already exists");
            client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.FAILURE, new AddSectionResponse(null)  );
            return;
        }

        // send success response
        client.sendMessage(MessageType.ADMIN_ADD_SECTION, MessageStatus.SUCCESS, new AddSectionResponse(newSection)  );

    }
    
    private void handleAdminGetUsers(Message<AdminGetUsersRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_USERS, MessageStatus.FAILURE, new AdminGetCampusesResponse(null));
            return;
        }
        
        client.sendMessage(MessageType.ADMIN_GET_USERS, MessageStatus.FAILURE, new AdminGetUsersResponse(null) );
    }

    private void handleAdminGetCampuses(Message<AdminGetCampusesRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_CAMPUSES, MessageStatus.FAILURE, new AdminGetCampusesResponse(null));
            return;
        }

        LinkedList<Campus> list = ((Admin)client.getUser()).getCampuses();
        client.sendMessage(MessageType.ADMIN_GET_CAMPUSES, MessageStatus.SUCCESS, new AdminGetCampusesResponse(list));
    
    }

    private void handleAdminGetDepartments(Message<AdminGetDepartmentsRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_DEPARTMENTS, MessageStatus.FAILURE, new AdminGetDepartmentsResponse(null));
            return;
        }

        LinkedList<Department> list = ((Admin)client.getUser()).getDepartments();
        client.sendMessage(MessageType.ADMIN_GET_DEPARTMENTS, MessageStatus.SUCCESS, new AdminGetDepartmentsResponse(list));
    
    }

    private void handleGetTerms(Message<GetTermsRequest> msg, ServerConnection client) {

        if(!client.isLoggedIn()){
            client.sendMessage(MessageType.GET_TERMS, MessageStatus.FAILURE, new GetTermsResponse(null));
            return;
        }

        LinkedList<Term> list = new LinkedList<>();
        for(Term term : Term.get()){
            list.Push(term.shallowCopy());
        }

        client.sendMessage(MessageType.GET_TERMS, MessageStatus.SUCCESS, new GetTermsResponse(list));
    
    }
    
    private void handleAdminGetCourses(Message<AdminGetCoursesRequest> msg, ServerConnection client) {
    
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_COURSES, MessageStatus.FAILURE, new AdminGetCoursesResponse(null));
            return;
        }

        LinkedList<Course> list = ((Admin)client.getUser()).getCourses();
        client.sendMessage(MessageType.ADMIN_GET_COURSES, MessageStatus.SUCCESS, new AdminGetCoursesResponse(list));
    
    }

    private void handleAdminGetSections(Message<AdminGetSectionsRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_SECTIONS, MessageStatus.FAILURE, new AdminGetSectionsResponse(null));
            return;
        }

        LinkedList<Section> list = ((Admin)client.getUser()).getSections();
        client.sendMessage(MessageType.ADMIN_GET_SECTIONS, MessageStatus.SUCCESS, new AdminGetSectionsResponse(list));
    
    }

    private void handleAdminRemoveUser(Message<AdminRemoveUserRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_REMOVE_USER, MessageStatus.FAILURE, new AdminRemoveUserResponse());
            return;
        }

        client.sendMessage(MessageType.ADMIN_REMOVE_USER, MessageStatus.FAILURE, new AdminRemoveUserResponse());
    }

    private void handleAdminRemoveCampus(Message<AdminRemoveCampusRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_REMOVE_CAMPUS, MessageStatus.FAILURE, new AdminRemoveCampusResponse());
            return;
        }

        String campusName = msg.get().campus();
        Campus campus = Campus.get(campusName);

        ((Admin)client.getUser()).removeCampus(campus);
        Campus.remove(campusName);

        client.sendMessage(MessageType.ADMIN_REMOVE_CAMPUS, MessageStatus.SUCCESS, new AdminRemoveCampusResponse());
    
    }

    private void handleAdminRemoveDepartment(Message<AdminRemoveDepartmentRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_REMOVE_DEPARTMENT, MessageStatus.FAILURE, new AdminRemoveDepartmentResponse());
            return;
        }

        String campusName = msg.get().campus();
        String departmentName = msg.get().department();
        Campus campus = Campus.get(campusName);
        Department department = campus.getDepartment(departmentName);

        ((Admin)client.getUser()).removeDepartment(department);
//        Campus.removeDepartment(department);

        client.sendMessage(MessageType.ADMIN_REMOVE_DEPARTMENT, MessageStatus.FAILURE, new AdminRemoveDepartmentResponse());
    }

    private void handleAdminRemoveCourse(Message<AdminRemoveCourseRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_REMOVE_COURSE, MessageStatus.FAILURE, new AdminRemoveCourseResponse());
            return;
        }

        client.sendMessage(MessageType.ADMIN_REMOVE_COURSE, MessageStatus.FAILURE, new AdminRemoveCourseResponse());
    }

    private void handleAdminRemoveSection(Message<AdminRemoveSectionRequest> msg, ServerConnection client) {
        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_REMOVE_SECTION, MessageStatus.FAILURE, new AdminRemoveSectionResponse());
            return;
        }

        client.sendMessage(MessageType.ADMIN_REMOVE_SECTION, MessageStatus.FAILURE, new AdminRemoveSectionResponse());
    }

    private void handleStudentBrowseSection(Message<BrowseSectionRequest> msg, ServerConnection client) {

        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.FAILURE, new BrowseSectionResponse( null ));
            return;
        }

        BrowseSectionRequest request = msg.get();
        
        String query = request.searchQuery();
        Campus campus = Campus.get(request.campus());
        Department department = campus.getDepartment(request.department());
        Term term = Term.get(request.term());
        int max_results = request.max_results();

        // must use array because no way to return a generic array from linked list
        if(term == null || campus == null || department == null || query.equals("")){
            client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.SUCCESS, new BrowseSectionResponse( null ) );
            return;
        }

        // linear search for relevant sections
        // search by class name 
        LinkedList<Section> sections = term.getSections();
        LinkedList<Section> results = new LinkedList<>();

        Log.Msg(sections.Length());

        int i = 0;

        for(Section section : sections){

            if(i++ > max_results){
                break;
            }

            if(!section.getDepartment().equals(department) || !section.getDepartment().getCampus().equals(campus)){
                continue;
            }

            if(section.Search(query)){
                Log.Msg("Adding section to search results!" + section);
                results.Push(section);
                continue;
            } 

        }

        BrowseSectionResponse res = new BrowseSectionResponse(results);
        client.sendMessage(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.SUCCESS, res);

    }

    private void handleStudentEnrollSection(Message<EnrollSectionRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0, EnrollStatus.USER_NOT_A_STUDENT));
            return;
        }

        EnrollSectionRequest request = msg.get();
        
        int sectionId = request.sectionId();
        Term term = Term.get(request.term());
        Student student = (Student)client.getUser();
        
        Section section = term.getSection(sectionId);
        if(section == null){
            Log.Err("Section does not exist");
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0,EnrollStatus.SECTION_NOT_FOUND));
            return;
        }
        
        // validate enrollment requirements
        if(!section.getCourse().verifyPrereqs(student)){
            Log.Err("Student not eligible for course");
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0,EnrollStatus.NOT_MET_REQUIREMENTS));
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
            Log.Err("Student schedule conflict");
                client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null,0,EnrollStatus.SCHEDULE_CONFLICT));
                return;
            }
    
        }

        // validate waitlist
        if(section.full()){
            Log.Err("Section waitlist full");
            
            // send waitlist response
            int waitlist_position = section.addWaitlist(student);
            student.addWaitlist(section);
            
            client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.FAILURE, new EnrollSectionResponse(null, waitlist_position, EnrollStatus.WAS_WAITLISTED));
            return;

        }

        Log.Err("Adding student to section!");

        // add student to section
        section.addStudent(student);
        student.addSection(section);
        
        assert(section.numStudents() > 0);
        Log.Err("Num students: " + section.numStudents());

        EnrollSectionResponse res = new EnrollSectionResponse(section, 0, EnrollStatus.WAS_ENROLLED);
        client.sendMessage(MessageType.STUDENT_ENROLL, MessageStatus.SUCCESS, res);

    }

    private void handleStudentDropSection(Message<DropSectionRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_DROP, MessageStatus.FAILURE, new DropSectionResponse(null));
            return;
        }
        
        DropSectionRequest request = msg.get();
        
        int sectionId = request.sectionId();
        Term term = Term.get(request.term());
        Student student = (Student)client.getUser();
        Section section = term.getSection(sectionId);

        // check if student is on waitlist instead of enrolled
        if(section.waitlisted(student)){
          
            section.removeWaitlist(student);
            student.removeWaitlist(section);

            // notify every student below this one that they moved up in waitlist?
            //for(Student s : section.getWaitlist()){
            //    s.Notify("Waitlist promotion!");
            //}

            client.sendMessage(MessageType.STUDENT_DROP, MessageStatus.SUCCESS, new DropSectionResponse(section));
            return;

        }
        
        section.removeStudent(student);
        student.removeSection(section);

        // waitlist logic
        if(section.waitlistLength() > 0){

            Student studentOnWaitlist = section.popWaitlist();
            
            section.addStudent(studentOnWaitlist);
            studentOnWaitlist.addSection(section);

            studentOnWaitlist.Notify("You have been enrolled!");

            // iterate students in wait list and notify them of waitlist promotion?
            //for(Student s : section.getWaitlist()){
            //    s.Notify("Your waitlist position has changed.");
            //}
       
        }

        DropSectionResponse res = new DropSectionResponse(section);
        client.sendMessage(MessageType.STUDENT_DROP, MessageStatus.SUCCESS, res);

    }

    private void handleStudentGetSchedule(Message<GetScheduleRequest> msg, ServerConnection client) {

        // Note: only for students?
        if(!client.validateStudent()){
            Log.Err("Get schedule: validateStudent failed. User is not Student");
            client.sendMessage(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.FAILURE, new GetScheduleResponse( null ));
            return;
        }

        Student student = (Student)client.getUser();
        if(student == null){
            Log.Err("Get schedule: Cast to Student failed. User is not Student");
            client.sendMessage(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.FAILURE, new GetScheduleResponse( null ));
            return;
        }

        Log.Msg("Sending schedule response");

        LinkedList<StudentScheduleItem> schedule = new LinkedList<>();

        // get currently enrolled sections 
        for(Section section : student.getEnrolledSections()){
            schedule.Push(new StudentScheduleItem(section, 0));
        }
        for(Section section : student.getWaitlistedSections()){
            schedule.Push(new StudentScheduleItem(section, section.getWaitlistPosition(student)));
        }

        GetScheduleResponse res = new GetScheduleResponse(schedule);
        
        client.sendMessage(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.SUCCESS, res);

    }

    private void handleStudentGetUnits(Message<StudentGetUnitRequest> msg, ServerConnection client){
        if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_GET_UNITS, MessageStatus.FAILURE, new StudentGetUnitResponse(0));
            return;
        }

        Student student = (Student)client.getUser();
        StudentGetUnitRequest request = msg.get();

        int totalUnits = 0;

        for(Section section : student.getEnrolledSections()){
            totalUnits += section.getCourse().getUnits();
        }

        StudentGetUnitResponse res = new StudentGetUnitResponse(totalUnits);
        client.sendMessage(MessageType.STUDENT_GET_UNITS, MessageStatus.SUCCESS, res);  
    }

    private void handleStudentGetProgress(Message<StudentGetProgressRequest> msg, ServerConnection client){
       if(!client.validateStudent()){
            client.sendMessage(MessageType.STUDENT_GET_PROGRESS, MessageStatus.FAILURE, new StudentGetProgressResponse(null,0));
            return;
        }

        Student student = (Student)client.getUser();

        LinkedList<Course> completed = student.getCompletedCourses();
        if(completed == null){
            completed = new LinkedList<>();
        }

        int completedUnits = 0;
        for(Course course : completed){
            completedUnits += course.getUnits();
        }

        StudentGetProgressResponse res = new StudentGetProgressResponse(completed, completedUnits);
        client.sendMessage(MessageType.STUDENT_GET_PROGRESS, MessageStatus.SUCCESS, res); 
    }

    private void handleLogout(Message<LogoutRequest> msg, ServerConnection client) {

        // send success response
        client.sendMessage(MessageType.USER_LOGOUT, MessageStatus.SUCCESS, new LogoutResponse[] { new LogoutResponse() } );
        
        // clear session
        client.setUser(null);

    }

    private void handleAdminGetReport(Message<ReportRequest> msg, ServerConnection client) {

        if(!client.validateAdmin()){
            client.sendMessage(MessageType.ADMIN_GET_REPORT, MessageStatus.FAILURE, new DisplayReport( null ));
            return;
        }

        ReportRequest request = msg.get();
        Term term = request.term();

        if(term == null){
            client.sendMessage(MessageType.ADMIN_GET_REPORT, MessageStatus.FAILURE, new DisplayReport( null ) );
            return;
        }

        LinkedList<String> reportEntries = new LinkedList<>();

        for(Section section : term.getSections()){
            Course course = section.getCourse();
            Department department = section.getDepartment();
            String campusName = department.getCampus().getName();
            String departmentName = department.getName();

            String reportLine = String.format(
                "%s %d - Section %d%n" +
                "Campus: %s%n" +
                "Department: %s%n" +
                "Enrollment %d/%d%n"  +
                "Waitlist: %d%n" +
                "Fill status: %.2f%% full%n" +
                "Change in enrollment: %.2f%%%n" +
                "Change in waitlist: %.2f%%%n%n",
                course.getName(),
                course.getNumber(),
                section.getNumber(),
                campusName,
                departmentName,
                section.numStudents(),
                section.getCapacity(),
                section.waitlistLength(),
                section.getPercentFull(),
                term.getEnrollmentPercentageChange(section),
                term.getWaitlistPercentageChange(section)

            );

            reportEntries.Push(reportLine);
        }

        client.sendMessage(MessageType.ADMIN_GET_REPORT, MessageStatus.SUCCESS, new DisplayReport(reportEntries));

    }

    public static boolean Serialize(String filepath, boolean save) {

        filepath = "server_data/" + filepath + ".ct";

        try {
            
            if(save) {
                FileOutputStream fileStream = new FileOutputStream(filepath);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                
                objectStream.writeInt(Campus.getNextId());
                objectStream.writeInt(Course.getNextId());
                objectStream.writeInt(Department.getNextId());
                objectStream.writeInt(Section.getNextId());
                objectStream.writeInt(Admin.getNextId());
                objectStream.writeInt(Student.getNextId());

                User.save(objectStream);
                Term.save(objectStream);
                Campus.save(objectStream);
            } else {
                FileInputStream fileStream = new FileInputStream(filepath);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                
                Campus.setNextId(objectStream.readInt());
                Course.setNextId(objectStream.readInt());
                Department.setNextId(objectStream.readInt());
                Section.setNextId(objectStream.readInt());
                Admin.setNextId(objectStream.readInt());
                Student.setNextId(objectStream.readInt());

                User.load(objectStream);
                Term.load(objectStream);
                Campus.load(objectStream);
            }
            return true;

        } catch (IOException e) {
                        
            e.printStackTrace();
            return false;
        
        }
    }

}
