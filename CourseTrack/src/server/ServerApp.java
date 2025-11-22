package server;

import global.Log;
import global.Message;

public class ServerApp {

	public static void main(String[] args) throws ClassNotFoundException {
		
		ServerController controller = ServerController.Get();
		int port = 7777;

		Server server = Server.Get();

		// set server callback callback functions

		// set callback for when Server receives a message
		server.OnRequest(
			(Message msg, ServerConnection client) -> {
				
				controller.handleMessage(msg, client);
				
			}
			
		);

		// set callback for when new client connects
		server.OnConnect(
			(ServerConnection client) -> {

				Log.Msg("Client has connected: " + client.getAddress());

			}
		);

		// set callback for when new client disconnects
		server.OnDisconnect(
			(ServerConnection client) -> {

				Log.Msg("Client has disconnected: " + client.getAddress());
			
			}
		);

		// launch server menu thread
		Shell shell = new Shell();
		new Thread(shell).start();

		// start server
        Log.Msg("Server Listening on " + port);
		server.Listen(port);

	}
}
