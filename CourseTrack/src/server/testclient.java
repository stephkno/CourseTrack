package server;

import client.*;
import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.data.*;
import global.requests.*;
import global.responses.*;

// test client for the server 
public class testclient {
    
    public static void main(String [] args) {

        var client = new Client("localhost", 7777);

		if (!client.connect()) {
            
			System.err.println("Could not connect to server.");
			return;

		}

		//var controller = new ClientController(client);
		//controller.start();

		String username = "testuser";
		String password = "password123";
		
		{
		
			client.sendRequest(new Message<>(MessageType.USER_REGISTER, MessageStatus.REQUEST, new RegisterRequest[]{new RegisterRequest(username, password, UserType.ADMIN)}));
			// await response
			System.out.println("Sending admin register request");
			Message<RegisterResponse> response = client.receiveResponse();
			System.out.println(response);	
			assert(response.getStatus() == MessageStatus.SUCCESS);
			System.out.println("Received admin register response");
		
		}
		client.User user;
		{

			System.out.println("Sending admin login request");
			client.sendRequest(new Message<>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new LoginRequest[]{ new LoginRequest(username, password) }));
			Message<LoginResponse> response = client.receiveResponse();
			assert(response.getStatus() == MessageStatus.SUCCESS);
			user = response.getArguments()[0].user();
			System.out.println(user);
			assert(user != null);
			System.out.println("Received admin login response");

		}
		{

			System.out.println("Sending admin add campus request");
			String campusName = "CSU East Bay";
			client.sendRequest(new Message<>(MessageType.ADMIN_ADD_CAMPUS, MessageStatus.REQUEST, new AddCampusRequest[]{ new AddCampusRequest(campusName)}));
			Message<AddCampusResponse> response = client.receiveResponse();
			assert(response.getStatus() == MessageStatus.SUCCESS);
			System.out.println("Received admin add campus response");

		}
		{

			System.out.println("Sending admin add department request");
			String campusName = "CSU East Bay";
			String departmentName = "Computer Science";
			client.sendRequest(new Message<>(MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.REQUEST, new AddDepartmentRequest[]{ new AddDepartmentRequest(campusName, departmentName)}));
			Message<AddDepartmentResponse> response = client.receiveResponse();
			assert(response.getStatus() == MessageStatus.SUCCESS);
			System.out.println("Received admin add department response");
		}

		int courseId = -1;

		{

			System.out.println("Sending admin add course request");

			AddCourseRequest newCourse = new AddCourseRequest(
				"Software Engineering",
				401,
				3,
				"CSU East Bay",
				"Computer Science"
			);

			client.sendRequest(new Message<>(MessageType.ADMIN_ADD_COURSE, MessageStatus.REQUEST, new AddCourseRequest[]{ newCourse }));
			
			Message<AddCourseResponse> response = client.receiveResponse();
			
			Log.Msg(response.toString());

			assert(response.getStatus() == MessageStatus.SUCCESS);

			Course responseCourse = response.getArguments()[0].course();
			System.out.println(newCourse.name());

			assert(responseCourse.getName().equals("Software Engineering"));
			
			courseId = responseCourse.getId();

			System.out.println("Received admin add course response");

		}
		{

			System.out.println("Sending admin add section request");

			AddSectionRequest newSection = new AddSectionRequest(
				courseId,
				"CSU East Bay",
				"Computer Science",
				new Term(Term.Season.SPRING, 2025),
				"Christopher Smith",
				30
			);

			client.sendRequest(new Message<>(MessageType.ADMIN_ADD_SECTION, MessageStatus.REQUEST, new AddSectionRequest[]{ newSection }));
			
			Message<AddSectionResponse> response = client.receiveResponse();
			
			Log.Msg(response.toString());

			assert(response.getStatus() == MessageStatus.SUCCESS);

			Section responseSection = response.getArguments()[0].section();
			System.out.println(responseSection.getNumber());

			assert(responseSection.getCourse().getName().equals("Software Engineering"));
			System.out.println("Received admin add section response");

		}
		{

			System.out.println("Sending logout request");

			LogoutRequest LogoutRequest = new LogoutRequest(
			);

			client.sendRequest(new Message<>(MessageType.USER_LOGOUT, MessageStatus.REQUEST, new LogoutRequest[]{ new LogoutRequest() }));
			
			Message<LogoutResponse> response = client.receiveResponse();
			assert(response.getStatus() == MessageStatus.SUCCESS);

			client.disconnect();

		}
		
		while(true) {}
		
    }

}
