package client;

import global.Log;

public class ClientApp  {
	
	public static void main(String[] args) {
		var client = new Client("localhost", 7777);

		if (!client.connect()) {
            Log.Err("Could not connect to server.");
			return;
		}

		var controller = new ClientController(client);
		controller.start();
		
	}
}
