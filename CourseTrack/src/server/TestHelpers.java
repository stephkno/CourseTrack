package server;

import client.*;
import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.data.*;
import global.requests.*;
import global.responses.*;
import global.LinkedList;

import java.time.LocalTime;

// test client for the server 
public class TestHelpers {
		
	static void RegisterUser(String username, String password, UserType type, Client client)
	{

		Log.Msg("Sending register request");
		
		Message<RegisterResponse> response = client.sendAndWait(
            new Message<RegisterRequest>(
				MessageType.USER_REGISTER, 
				MessageStatus.REQUEST, 
				new RegisterRequest(username, password, type)
			)
        );

		Log.Msg(response);	
		assert(response.getStatus() == MessageStatus.SUCCESS);
		Log.Msg("Received register response " + response.getStatus());
	
	}

	static LinkedList<Campus> GetCampuses(Client client)
	{

		Log.Msg("Sending get campuses request");
		
		Message<GetCampusesResponse> response = client.sendAndWait(
            new Message<GetCampusesRequest>(
				MessageType.GET_CAMPUSES, 
				MessageStatus.REQUEST, 
				new GetCampusesRequest()
			)
        );

		Log.Msg(response);	
		assert(response.getStatus() == MessageStatus.SUCCESS);

		Log.Msg("Received get campuses response " + response.get());
		return response.get().campuses();
	
	}

	static client.User Login(String username, String password, Client client)
	{ 
		if (!client.isConnected()) {
			if (!client.connect()) {
				System.err.println("Could not connect to server.");
				return null;
			}
		}

		Log.Msg("Sending login request"); 
		Message<LoginResponse> response = client.sendAndWait(new Message<>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new LoginRequest(username, password) )); 
		
		assert(response.getStatus() == MessageStatus.SUCCESS); 
		
		client.User user = response.get().user(); Log.Msg(user); 

		assert(user != null); Log.Msg("Received login response"); 
		
		return user;

	}


	static void AddCampus(String campusName, Client client)
	{

		Log.Msg("Sending admin add campus request");
		Message<AddCampusResponse> response = client.sendAndWait(new Message<>(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.REQUEST, new AddCampusRequest(campusName) ));
		
		assert(response.getStatus() == MessageStatus.SUCCESS);

		Log.Msg("Received admin add campus response: " + response.getStatusString());

	}

	static void AddDepartment(String campusName, String departmentName, Client client)
	{

		Log.Msg("Sending admin add department request");
		Message<AddDepartmentResponse> response = client.sendAndWait(new Message<>(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.REQUEST, new AddDepartmentRequest(campusName, departmentName) ));
		
		assert(response.getStatus() == MessageStatus.SUCCESS);
		Log.Msg("Received admin add department response: " + response.getStatusString());
	}
	
	private static int AddCourse(String courseName, int number, int units, String campus, String department, Client client)
	{
		Log.Msg("Sending admin add course request");

		AddCourseRequest newCourse = new AddCourseRequest(
			courseName,
			number,
			units,
			campus,
			department
		);

		Message<AddCourseResponse> response = client.sendAndWait(new Message<>(MessageType.ADMIN_ADD_COURSE, MessageStatus.REQUEST, newCourse ));
		
		Log.Msg(response.toString());

		assert(response.getStatus() == MessageStatus.SUCCESS);

		Course responseCourse = response.get().course();
		
		assert(responseCourse != null);
		assert(responseCourse.getName().equals("Software Engineering"));
		
		Log.Msg(newCourse.name());

		int courseId = responseCourse.getId();

		Log.Msg("Received admin add course response: " + response.getStatusString());
		
		return courseId;

	}

	private static void AddSection(int courseId, String campus, String department, Term term, String instructor, int capacity, MeetTime[] meetTimes, Client client)
	{

		Log.Msg("Sending admin add section request");

		AddSectionRequest newSection = new AddSectionRequest(
			courseId,
			campus,
			department,
			term,
			instructor,
			capacity,
			meetTimes
		);

		Message<AddSectionResponse> response = client.sendAndWait(new Message<>(MessageType.ADMIN_ADD_SECTION, MessageStatus.REQUEST, newSection ));
		
		Log.Msg(response.toString());

		assert(response.getStatus() == MessageStatus.SUCCESS);

		Section responseSection = response.get().section();
		Log.Msg(responseSection.getNumber());

		Log.Msg("Received admin add section response: " + response.getStatusString());

	}

	private static LinkedList<Section> SearchCourses(String searchQuery, String campusName, String departmentName, Term term, Client client){

		Message<BrowseSectionResponse> response = client.sendAndWait(new Message<BrowseSectionRequest>(MessageType.STUDENT_BROWSE_SECTION, MessageStatus.REQUEST, new BrowseSectionRequest(
			searchQuery,
			campusName,
			departmentName,
			term,
			32
		)));
		
		BrowseSectionResponse data = response.get();

		assert(response.getStatus() == MessageStatus.SUCCESS);

		return data.sections();

	}

	private static Section Enroll(int sectionId, Term term, Client client){

		Message<EnrollSectionResponse> response = client.sendAndWait(new Message<EnrollSectionRequest>(MessageType.STUDENT_ENROLL, MessageStatus.REQUEST, new EnrollSectionRequest(
			sectionId, 
			term
		)));
		
		EnrollSectionResponse data = response.get();

		assert(response.getStatus() == MessageStatus.SUCCESS);

		Log.Msg(response);

		return response.get().section();
	}
	
	private static Section Drop(int sectionId, Term term, Client client){

		Message<DropSectionResponse> response = client.sendAndWait(new Message<DropSectionRequest>(MessageType.STUDENT_DROP, MessageStatus.REQUEST, new DropSectionRequest(
			sectionId, 
			term
		)));

		DropSectionResponse data = response.get();

		assert(response.getStatus() == MessageStatus.SUCCESS);

		Log.Msg(response);

		return response.get().section();

	}
	
	private static void GetSchedule(Term term, Client client){

		Message<GetScheduleResponse> response = client.sendAndWait(new Message<GetScheduleRequest>(MessageType.STUDENT_GET_SCHEDULE, MessageStatus.REQUEST, new GetScheduleRequest(
			term
		)));

		GetScheduleResponse data = response.get();

		assert(response.getStatus() == MessageStatus.SUCCESS);

		Log.Msg("Received schedule response");

		for(Section s : data.enrolledSections()){
			Log.Msg(s);
		}

	}

	private static void Logout(Client client)
	{

		Log.Msg("Sending logout request");
		Message<BrowseSectionResponse> response = client.sendAndWait(new Message<LogoutRequest>(MessageType.USER_LOGOUT, MessageStatus.REQUEST, new LogoutRequest() ));
		
		assert(response.getStatus() == MessageStatus.SUCCESS);
		Log.Msg("Received logout response");
		
	}

	static Client client = new Client("localhost", 7777);

	static void StartClient(){

		if (!client.isConnected()) {
			
			if (!client.connect()) {
				Log.Err("Could not connect to server.");
				return;
			}

		}
		
		ClientController controller = new ClientController(client);
		controller.start();
	
	}

    public static void AddTestData() {

		Log.Msg("Adding server test data");
		
		StartClient();
		
		String username = "testuser";
		String password = "password123";
		RegisterUser(username, password, UserType.ADMIN, client);
		Login(username, password, client);

		AddCampus("CSU East Bay", client);
		
		AddDepartment("CSU East Bay", "CS", client);

		int id = AddCourse("CS 1", 101, 3, "CSU East Bay", "CS", client);
		int id1 = AddCourse("Computing Science 2", 201, 3, "CSU East Bay", "CS", client);
		int id2 = AddCourse("Computing and Social Responsibility", 230, 3, "CSU East Bay", "CS", client);
		int id3 = AddCourse("Computing Organization and Assembly Language", 221, 3, "CSU East Bay", "CS", client);
		int id4 = AddCourse("Discrete Structures", 211, 3, "CSU East Bay", "CS", client);
		int id5 = AddCourse("Programming Language Concepts", 311, 3, "CSU East Bay", "CS", client);
		int id6 = AddCourse("Computer Architecture", 321, 3, "CSU East Bay", "CS", client);
		int id7 = AddCourse("Data Structures and Algorithms", 301, 3, "CSU East Bay", "CS", client);
		int id8 = AddCourse("Automata and Computation", 411, 3, "CSU East Bay", "CS", client);
		int id9 = AddCourse("Software Engineering", 401, 3, "CSU East Bay", "CS", client);
		int id10 = AddCourse("Operating Systems", 421, 3, "CSU East Bay", "CS", client);
		int id11 = AddCourse("Computer Networks", 441, 3, "CSU East Bay", "CS", client);
		int id12 = AddCourse("Analysis of Algorithms", 413, 3, "CSU East Bay", "CS", client);
		
		int id13 = AddCourse("TestCourse", 555, 3, "CSU East Bay", "CS", client);

		MeetTime[] meetTimes = new MeetTime[]{
			new MeetTime(MeetTime.Day.MONDAY, LocalTime.of(14, 30), LocalTime.of(14, 30)),
			new MeetTime(MeetTime.Day.MONDAY, LocalTime.of(14, 30), LocalTime.of(14, 30))
		};
		
		Term term = new Term(Term.Season.FALL, 2025);

		AddSection(id, "CSU East Bay", "CS", term, "Alice Johnson", 30, meetTimes, client);
		AddSection(id1, "CSU East Bay", "CS", term, "Bob Martinez", 25, meetTimes, client);
		AddSection(id2, "CSU East Bay", "CS",  term,"Carol Lee", 35, meetTimes, client);
		AddSection(id3, "CSU East Bay", "CS", term,"David Kim", 28,  meetTimes, client);
		AddSection(id4, "CSU East Bay", "CS", term,"Emily Chen", 32,  meetTimes, client);
		AddSection(id5, "CSU East Bay", "CS", term,"Frank Brown", 30,  meetTimes, client);
		AddSection(id6, "CSU East Bay", "CS", term,"Grace Wilson", 27,  meetTimes, client);
		AddSection(id7, "CSU East Bay", "CS", term,"Henry Clark", 33,  meetTimes, client);
		AddSection(id8, "CSU East Bay", "CS", term,"Isabella Davis", 29,  meetTimes, client);
		AddSection(id9, "CSU East Bay", "CS", term,"Jack Miller", 30,  meetTimes, client);
		AddSection(id10, "CSU East Bay", "CS", term, "Karen Taylor", 26,  meetTimes, client);
		AddSection(id11, "CSU East Bay", "CS", term, "Liam Anderson", 34,  meetTimes, client);
		AddSection(id12, "CSU East Bay", "CS", term, "Mia Thomas", 31,  meetTimes, client);
		AddSection(id13, "CSU East Bay", "CS", term, "TestInstructor", 2,  meetTimes, client);

		Logout(client);

		RegisterUser("teststudent", "password", UserType.STUDENT, client);
		
		Login("teststudent", "password", client);

		LinkedList<Section> sections = SearchCourses("softwa", "CSU East Bay", "CS", term, client);
		
		Log.Msg("Search results:");

		for(Section section : sections){
			if(section == null) continue;
			Log.Msg(section);
		}
		
		Section enrolledSection = Enroll(sections.Get(0).getId(), sections.Get(0).getTerm(), client);
		Log.Msg("Received enroll response! Num students: " + enrolledSection.numStudents());
		assert(enrolledSection.numStudents() == 1);

		Section droppedSection = Drop(sections.Get(0).getId(), sections.Get(0).getTerm(), client);
		assert(droppedSection.numStudents() == 1);
		
		Logout(client);

		LinkedList<Campus> campuses = GetCampuses(client);
		for(Campus campus : campuses){
			Log.Msg(campus.toString());
		}
    }

	public static void main(String[] args){
		AddTestData();
	}
}
