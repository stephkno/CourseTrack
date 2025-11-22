package server;

import client.*;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.requests.*;
import global.responses.*;
import server.data.*;

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
			Campus responseCampus = response.getArguments()[0].campus();
			System.out.println(responseCampus);
			assert(responseCampus.getCampusName().equals(campusName));
			System.out.println("Received admin add campus response");

		}
		{

			System.out.println("Sending admin add course request");

			AddCourseRequest newCourse = new AddCourseRequest(
				"Software Engineering",
				401,
				3,
				"Computer Science",
				"CSU East Bay"
			);

			client.sendRequest(new Message<>(MessageType.ADMIN_ADD_COURSE, MessageStatus.REQUEST, new AddCourseRequest[]{ newCourse }));
			
			Message<AddCourseResponse> response = client.receiveResponse();
			assert(response.getStatus() == MessageStatus.SUCCESS);

			Course responseCourse = response.getArguments()[0].course();
			System.out.println(newCourse.name());

			assert(responseCourse.getName().equals("Software Engineering"));
			System.out.println("Received admin add course response");

		}
		
		/*
		
			3.1.2. Administrator Functions

			3.1.2.1 Administrator can create/read/update/delete student accounts ? 

			3.1.2.2 Administrator can create/read/update/delete universities/campuses x

			3.1.2.3 Administrator can create/read/update/delete new term schedules of classes x

			3.1.2.4 Administrator can create/read/update/delete classes in term x

			3.1.2.5 Administrator can add schedule data by loading a formatted data file ?
			
		*/

		while(true) {}
		
    }

}
