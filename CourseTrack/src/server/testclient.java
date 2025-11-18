package server;

import client.*;
import client.requests.*;
import client.responses.*;
import global.Message;
import global.MessageStatus;
import global.MessageType;

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
			String responseCampusName = response.getArguments()[0].msg();
			System.out.println(responseCampusName);
			assert(responseCampusName.equals(campusName));
			System.out.println("Received admin add campus response");

		}
		
		while(true){}
		
    }

}
