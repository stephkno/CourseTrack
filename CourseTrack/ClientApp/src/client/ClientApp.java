package client;

public class ClientApp {
	public static int main(String[] args) {
		var client = new Client("localhost", 7777);

		if (!client.connect()) {
            System.err.println("Could not connect to server.");
			return -1;
		}

		var controller = new ClientController(client);
		controller.start();

		return 0;
	}
}
