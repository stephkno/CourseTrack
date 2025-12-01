package server;

import client.*;

import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.data.*;
import global.requests.*;
import global.requests.AdminRequests.*;
import global.responses.*;
import global.responses.AdminResponses.*;
import global.LinkedList;

import java.time.LocalTime;

public class TestHelpers {

    static Client client = new Client("localhost", 7777);

    static void StartClient() {
        if (!client.isConnected()) {
            if (!client.connect()) {
                Log.Err("Could not connect to server.");
                return;
            }
        }
        ClientController controller = new ClientController(client);
        controller.start();
    }

    static void RegisterUser(String username, String password, UserType type, Client client) {
        Log.Msg("Sending register request");
        Message<RegisterResponse> response = client.sendAndWait(
            new Message<>(MessageType.USER_REGISTER, MessageStatus.REQUEST, new RegisterRequest(username, password, type))
        );
        Log.Msg(response);
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received register response " + response.getStatus());
    }

    static LinkedList<Campus> GetCampuses(Client client) {
        Log.Msg("Sending get campuses request");
        Message<GetCampusesResponse> response = client.sendAndWait(
            new Message<>(MessageType.GET_CAMPUSES, MessageStatus.REQUEST, new GetCampusesRequest())
        );
        Log.Msg(response);
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received get campuses response " + response.get());
        return response.get().campuses();
    }

    static void RemoveCampus(String campusName, Client client) {
        Log.Msg("Sending admin remove campus request");
        Message<AdminRemoveCampusResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_REMOVE_CAMPUS, MessageStatus.REQUEST, new AdminRemoveCampusRequest(campusName))
        );
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received admin remove campus response: " + response.getStatus());
    }

    static client.User Login(String username, String password, Client client) {
        if (!client.isConnected()) {
            if (!client.connect()) {
                System.err.println("Could not connect to server.");
                return null;
            }
        }
        Log.Msg("Sending login request");
        Message<LoginResponse> response = client.sendAndWait(
            new Message<>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new LoginRequest(username, password))
        );
        assert(MessageStatus.SUCCESS == response.getStatus());
        client.User user = response.get().user();
        Log.Msg(user);
        assert(user != null);
        Log.Msg("Received login response");
        return user;
    }

    static void AddCampus(String campusName, Client client) {
        Log.Msg("Sending admin add campus request");
        Message<AddCampusResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.REQUEST, new AddCampusRequest(campusName))
        );
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received admin add campus response: " + response.getStatus());
    }

    static void AddDepartment(String campusName, String departmentName, Client client) {
        Log.Msg("Sending admin add department request");
        Message<AddDepartmentResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.REQUEST, new AddDepartmentRequest(campusName, departmentName))
        );
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received admin add department response: " + response.getStatus());
    }

    private static Course AddCourse(String courseName, int number, int units, String campus, String department, LinkedList<Course> requirements, Client client) {
        Log.Msg("Sending admin add course request");
        AddCourseRequest newCourse = new AddCourseRequest(courseName, number, units, campus, department, requirements);
        Message<AddCourseResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_ADD_COURSE, MessageStatus.REQUEST, newCourse)
        );
        Log.Msg(response.toString());
        assert(MessageStatus.SUCCESS == response.getStatus());
        Course responseCourse = response.get().course();
        assert(responseCourse != null);
        Log.Msg(newCourse.name());
        Log.Msg("Received admin add course response: " + response.getStatus());
        return responseCourse;
    }

    private static void AddSection(Course course, String campus, String department, Term term, String instructor, int capacity, MeetTime[] meetTimes, Client client) {
        Log.Msg("Sending admin add section request");
        AddSectionRequest newSection = new AddSectionRequest(course.getId(), campus, department, term, instructor, capacity, meetTimes);
        Message<AddSectionResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_ADD_SECTION, MessageStatus.REQUEST, newSection)
        );
        Log.Msg(response.toString());
        assert(MessageStatus.SUCCESS == response.getStatus());
        Section responseSection = response.get().section();
        Log.Msg(responseSection.getNumber());
        Log.Msg("Received admin add section response: " + response.getStatus());
    }

    private static LinkedList<Section> SearchCourses(String searchQuery, String campusName, String departmentName, Term term, Client client) {
        Message<BrowseSectionResponse> response = client.sendAndWait(
            new Message<>(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.REQUEST, new BrowseSectionRequest(searchQuery, campusName, departmentName, term, 32))
        );
        BrowseSectionResponse data = response.get();
        assert(MessageStatus.SUCCESS == response.getStatus());
        return data.sections();
    }

    private static Section Enroll(int sectionId, Term term, Client client) {
        Message<EnrollSectionResponse> response = client.sendAndWait(
            new Message<>(MessageType.STUDENT_ENROLL, MessageStatus.REQUEST, new EnrollSectionRequest(sectionId, term))
        );
        EnrollSectionResponse data = response.get();
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg(response);
        return data.section();
    }

    private static Section Drop(int sectionId, Term term, Client client) {
        Message<DropSectionResponse> response = client.sendAndWait(
            new Message<>(MessageType.STUDENT_DROP, MessageStatus.REQUEST, new DropSectionRequest(sectionId, term))
        );
        DropSectionResponse data = response.get();
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg(response);
        return data.section();
    }

    private static void GetSchedule(Term term, Client client) {
        Message<GetScheduleResponse> response = client.sendAndWait(
            new Message<>(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.REQUEST, new GetScheduleRequest(term))
        );
        GetScheduleResponse data = response.get();
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received schedule response");
        for (StudentScheduleItem item : data.schedule()) {
            Log.Msg(item.toString());
        }
    }

    private static void AdminGetCourses(Client client) {
        Message<AdminGetCoursesResponse> response = client.sendAndWait(
            new Message<>(MessageType.ADMIN_GET_COURSES, MessageStatus.REQUEST, new AdminGetCoursesRequest())
        );
        AdminGetCoursesResponse data = response.get();
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received GetCourses response");
        for (Course s : data.courses()) {
            Log.Msg(s);
        }
    }

    private static void Logout(Client client) {
        Log.Msg("Sending logout request");
        Message<BrowseSectionResponse> response = client.sendAndWait(
            new Message<>(MessageType.USER_LOGOUT, MessageStatus.REQUEST, new LogoutRequest())
        );
        assert(MessageStatus.SUCCESS == response.getStatus());
        Log.Msg("Received logout response");
    }

    public static void addTestData() {
        
        StartClient();

        String username = "admin";
        String password = "";
        
        RegisterUser(username, password, UserType.ADMIN, client);
        Login(username, password, client);
        
        MeetTime[] meetTimes = new MeetTime[]{
            new MeetTime(MeetTime.Day.MONDAY, LocalTime.of(14, 30), LocalTime.of(14, 30)),
            new MeetTime(MeetTime.Day.WEDNESDAY, LocalTime.of(14, 30), LocalTime.of(14, 30))
        };
        Term term = new Term(Term.Season.FALL, 2025);

        AddCampus("CSU East Bay", client);
        AddDepartment("CSU East Bay", "CS", client);
        AddDepartment("CSU East Bay", "ART", client);
        AddDepartment("CSU East Bay", "SOC", client);
        AddDepartment("CSU East Bay", "PSY", client);
        AddDepartment("CSU East Bay", "BIO", client);

        AddCampus("SFSU", client);
        AddDepartment("SFSU", "CS", client);
        AddDepartment("SFSU", "ART", client);
        AddDepartment("SFSU", "PSY", client);

        AddCampus("CSU Long Beach", client);
        AddDepartment("CSU Long Beach", "CS", client);
        AddDepartment("CSU Long Beach", "ENG", client);
        AddDepartment("CSU Long Beach", "MATH", client);

        AddCampus("CSU Fullerton", client);
        AddDepartment("CSU Fullerton", "CS", client);
        AddDepartment("CSU Fullerton", "BUS", client);
        AddDepartment("CSU Fullerton", "PSY", client);

        AddCampus("CSU San Marcos", client);
        AddDepartment("CSU San Marcos", "CS", client);
        AddDepartment("CSU San Marcos", "BIO", client);
        AddDepartment("CSU San Marcos", "CHEM", client);

        LinkedList<Course> csuEastBayRequirements = new LinkedList<>();
        Course csEB101 = AddCourse("Intro to Programming", 101, 3, "CSU East Bay", "CS", csuEastBayRequirements, client);
        csuEastBayRequirements.Push(csEB101);
        Course csEB102 = AddCourse("Data Structures", 201, 3, "CSU East Bay", "CS", csuEastBayRequirements, client);
        csuEastBayRequirements.Push(csEB102);
        Course csEB201 = AddCourse("Algorithms", 301, 3, "CSU East Bay", "CS", csuEastBayRequirements, client);
        csuEastBayRequirements.Push(csEB201);
        Course csEB301 = AddCourse("Operating Systems", 401, 3, "CSU East Bay", "CS", csuEastBayRequirements, client);

        LinkedList<Course> sfsuRequirements = new LinkedList<>();
        Course sfsuCS101 = AddCourse("Programming 1", 101, 3, "SFSU", "CS", sfsuRequirements, client);
        sfsuRequirements.Push(sfsuCS101);
        Course sfsuCS102 = AddCourse("Programming 2", 102, 3, "SFSU", "CS", sfsuRequirements, client);
        sfsuRequirements.Push(sfsuCS102);
        Course sfsuCS201 = AddCourse("Algorithms", 201, 3, "SFSU", "CS", sfsuRequirements, client);
        sfsuRequirements.Push(sfsuCS201);

        LinkedList<Course> csulbRequirements = new LinkedList<>();
        Course csulbCS101 = AddCourse("Intro to CS", 101, 3, "CSU Long Beach", "CS", csulbRequirements, client);
        csulbRequirements.Push(csulbCS101);
        Course csulbCS102 = AddCourse("Data Structures & Algorithms", 102, 3, "CSU Long Beach", "CS", csulbRequirements, client);
        csulbRequirements.Push(csulbCS102);
        Course csulbCS201 = AddCourse("Software Engineering", 201, 3, "CSU Long Beach", "CS", csulbRequirements, client);

        LinkedList<Course> csufRequirements = new LinkedList<>();
        Course csufCS101 = AddCourse("Intro to Programming", 101, 3, "CSU Fullerton", "CS", csufRequirements, client);
        csufRequirements.Push(csufCS101);
        Course csufCS102 = AddCourse("Object-Oriented Programming", 102, 3, "CSU Fullerton", "CS", csufRequirements, client);
        csufRequirements.Push(csufCS102);
        Course csufCS201 = AddCourse("Databases", 201, 3, "CSU Fullerton", "CS", csufRequirements, client);

        LinkedList<Course> csusmRequirements = new LinkedList<>();
        Course csusmCS101 = AddCourse("Intro to Programming", 101, 3, "CSU San Marcos", "CS", csusmRequirements, client);
        csusmRequirements.Push(csusmCS101);
        Course csusmCS102 = AddCourse("Data Structures", 102, 3, "CSU San Marcos", "CS", csusmRequirements, client);
        csusmRequirements.Push(csusmCS102);
        Course csusmCS201 = AddCourse("Algorithms", 201, 3, "CSU San Marcos", "CS", csusmRequirements, client);

        // --- Sections ---
        MeetTime[] defaultTimes = new MeetTime[]{
            new MeetTime(MeetTime.Day.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 15)),
            new MeetTime(MeetTime.Day.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 15))
        };
        Term fall2025 = new Term(Term.Season.FALL, 2025);

        // CSU East Bay Sections
        AddSection(csEB101, "CSU East Bay", "CS", fall2025, "Dr. Alan Turing", 30, defaultTimes, client);
        AddSection(csEB102, "CSU East Bay", "CS", fall2025, "Dr. Ada Lovelace", 25, defaultTimes, client);
        AddSection(csEB201, "CSU East Bay", "CS", fall2025, "Dr. Grace Hopper", 28, defaultTimes, client);
        AddSection(csEB301, "CSU East Bay", "CS", fall2025, "Dr. Donald Knuth", 32, defaultTimes, client);

        // SFSU Sections
        AddSection(sfsuCS101, "SFSU", "CS", fall2025, "Dr. Linus Torvalds", 35, defaultTimes, client);
        AddSection(sfsuCS102, "SFSU", "CS", fall2025, "Dr. Tim Berners-Lee", 30, defaultTimes, client);
        AddSection(sfsuCS201, "SFSU", "CS", fall2025, "Dr. Barbara Liskov", 28, defaultTimes, client);

        // CSU Long Beach Sections
        AddSection(csulbCS101, "CSU Long Beach", "CS", fall2025, "Dr. John McCarthy", 40, defaultTimes, client);
        AddSection(csulbCS102, "CSU Long Beach", "CS", fall2025, "Dr. Margaret Hamilton", 35, defaultTimes, client);
        AddSection(csulbCS201, "CSU Long Beach", "CS", fall2025, "Dr. Edsger Dijkstra", 30, defaultTimes, client);

        // CSU Fullerton Sections
        AddSection(csufCS101, "CSU Fullerton", "CS", fall2025, "Dr. Brian Kernighan", 28, defaultTimes, client);
        AddSection(csufCS102, "CSU Fullerton", "CS", fall2025, "Dr. Dennis Ritchie", 25, defaultTimes, client);
        AddSection(csufCS201, "CSU Fullerton", "CS", fall2025, "Dr. Niklaus Wirth", 30, defaultTimes, client);

        // CSU San Marcos Sections
        AddSection(csusmCS101, "CSU San Marcos", "CS", fall2025, "Dr. Richard Stallman", 32, defaultTimes, client);
        AddSection(csusmCS102, "CSU San Marcos", "CS", fall2025, "Dr. Ken Thompson", 28, defaultTimes, client);
        AddSection(csusmCS201, "CSU San Marcos", "CS", fall2025, "Dr. ", 25, defaultTimes, client);

        AdminGetCourses(client);
        
        Logout(client);
        
        RegisterUser("student", "", UserType.STUDENT, client);
        Login("student", "", client);

        LinkedList<Section> sections = SearchCourses("data str", "CSU East Bay", "CS", term, client);
        for (Section section : sections) {
            if (section == null) continue;
            Log.Msg(section);
        }

        Section enrolledSection = Enroll(sections.Get(0).getId(), sections.Get(0).getTerm(), client);
        assert(1 == enrolledSection.numStudents());
        
        Section droppedSection = Drop(sections.Get(0).getId(), sections.Get(0).getTerm(), client);
        assert(1 == droppedSection.numStudents());
        
        GetSchedule(term, client);
        
        LinkedList<Campus> campuses = GetCampuses(client);
        for (Campus campus : campuses) {
            Log.Msg(campus.toString());
        }
        
        Logout(client);
        Login("admin", "", client);
       // RemoveCampus("CSU East Bay", client);

        Logout(client);

    }

    public static void main(String[] args) {
        addTestData();
    }

}
