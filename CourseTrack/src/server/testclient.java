package server;

import client.*;
import client.requests.*;
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

		var controller = new ClientController(client);
		controller.start();
		
		String username = "testuser";
		String password = "password123";
		
		client.sendRequest(new Message<>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new RegisterRequest[]{new RegisterRequest(username, password, UserType.STUDENT)}));
		controller.sendLoginRequest("testuser", "password123");

    }

}