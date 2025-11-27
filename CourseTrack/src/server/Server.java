package server;

import global.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


// serverSocket handler manages the serverSocket and owns all client ServerSession connections
public class Server {

	// server class
	private int port;
	private boolean running = true;

	private LocalDateTime start;

	public String Uptime() {
		
		LocalDateTime end = LocalDateTime.now();

		String outstring = "Uptime:";

		long days = ChronoUnit.DAYS.between(start, end);
		long hours = ChronoUnit.HOURS.between(start, end);
		long mins = ChronoUnit.MINUTES.between(start, end);
		long secs = ChronoUnit.SECONDS.between(start, end);

		outstring += days + " days, " + hours + " hours, " + mins + " mins, and " + secs + " seconds.";

		return outstring;

	}

	// server incoming connection listener
	private ServerSocket serverSocket = null;

	// list of connected clients
	private HashMap<String, ServerConnection> clients = new HashMap<>();
	
	// callback functions
	private Callback_T_U<Message, ServerConnection> requestCallback;
	private Callback_T<ServerConnection> connectCallback;
	private Callback_T<ServerConnection> disconnectCallback;

	// singleton
	private Server() {}
	static Server server = new Server();
	static Server Get() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public static boolean Running() {
		return server.running;
	}

	public void Hangup() {
		
		running = false;
		
		for(ServerConnection client : clients) {

			client.Hangup();
		
		}

		try {
		
			serverSocket.close();  // may throw IOException
		
		} catch (IOException e) {
		
			e.printStackTrace();  // or log it
		
		}

	}

	String MessageStats(){
		String outString = "";
		outString += "\nMessages received:";
		outString += "\nMessages sent:";
		return outString;
	}

	String MemoryUsage(){
		String outString = "";
		long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		outString += String.valueOf(mem);
		return outString;
	}

	public void OnConnect(Callback_T<ServerConnection> connectCallback) {	
		this.connectCallback = connectCallback;
	}

	public void OnDisconnect(Callback_T<ServerConnection> disconnectCallback) {
		this.disconnectCallback = disconnectCallback;
	}

	public void OnRequest(Callback_T_U<Message, ServerConnection> requestCallback) {
		this.requestCallback = requestCallback;
	}

	public String[] getClients() {

		int num_clients = clients.Size();
		String[] out_strings = new String[num_clients];
		int i = 0;

		for(ServerConnection client : clients){
			out_strings[i++] = client.getAddress();
		}

		return out_strings;
	}

	public ServerConnection getClient(String key) {
		return clients.Get(key);
	}

	// called to remove client from the server
	public boolean RemoveClient(ServerConnection client) {
		
		String address = client.getAddress();
		return clients.Remove(address);

	}

	// send a message to all connected clients
	public void Emit(Message<?> msg) {

		for(ServerConnection client : clients) client.Send(msg);
	
	}
	
	// start server
	public void Listen(int port) {

		start = LocalDateTime.now();

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

			Log.Msg("Starting server listener...");

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

				clients.Put(newConnection.getAddress(), newConnection);
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
