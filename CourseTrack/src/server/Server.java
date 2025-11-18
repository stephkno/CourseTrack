package server;

import global.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

// serverSocket handler manages the serverSocket and owns all client ServerSession connections
public class Server{

	// server class
	private int port;
	private boolean running = false;

	// server incoming connection listener
	private ServerSocket serverSocket = null;

	// list of connected clients
	private HashMap<ServerConnection> clients = new HashMap<>();
	
	// callback functions
	private RequestCallback<Message, ServerConnection> requestCallback;
	private Callback<ServerConnection> connectCallback;
	private Callback<ServerConnection> disconnectCallback;

	// singleton
	private Server() {}
	static Server server = new Server();
	static Server Get() {
		return server;
	}

	public int GetPort(){
		return port;
	}
	
	public void OnConnect(Callback<ServerConnection> connectCallback) {	
		this.connectCallback = connectCallback;
	}

	public void OnDisconnect(Callback<ServerConnection> disconnectCallback) {
		this.disconnectCallback = disconnectCallback;
	}

	public void OnRequest(RequestCallback<Message, ServerConnection> requestCallback) {
		this.requestCallback = requestCallback;
	}

	// called to remove client from the server
	public boolean RemoveClient(ServerConnection client) {
		
		String address = client.GetAddress();
		return clients.Remove(address);

	}

	// send a message to all connected clients
	public void Emit(Message<?> msg) {

		for(ServerConnection client : clients) client.Send(msg);
	
	}
	
	// start server
	public void Listen(int port) {

		if(disconnectCallback == null) {
			Log.Err("Disconnect callback is not set.");
			return;
		}
		if(requestCallback == null) {
			Log.Err("Message callback is not set.");
			return;
		}

		try {

			this.port = port;
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			Log.Msg("Listening on port " + port);

			running = true;

			// running infinite loop for getting
			// client request
			while (running) {

				// socket object to receive incoming client
				// requests
				Socket socket = serverSocket.accept();
				
				// create a new thread object
				ServerConnection newConnection = new ServerConnection(socket, server,
					// register callback Callbacktions
					(Message message, ServerConnection client) -> {
						requestCallback.call(message, client);
					},
					disconnectCallback
				);

				clients.Put(newConnection.GetAddress(), newConnection);
				connectCallback.call(newConnection);

				// This thread will handle the client
				// separately
				new Thread(newConnection).start();
			
			}
		
		}
		catch(BindException e) {
			e.printStackTrace();
			Log.Err("Port " + port + " is already in use. Please choose another port.");
			running = false;
		}
		catch (IOException e) {
			e.printStackTrace();
			running = false;
		}
		finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
					running = false;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
