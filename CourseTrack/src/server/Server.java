package server;

import client.requests.*;
import global.*;
import java.awt.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// serverSocket handler manages the serverSocket and owns all client ServerSession connections
public class Server{

	// server class
	int port;

	// server incoming connection listener
	ServerSocket serverSocket = null;

	// list of connected clients
	HashMap<ServerConnection> clients = new HashMap<>();
	
	// callback functions
	private RequestCallback<Message, ServerConnection> requestCallback;
	private Callback<ServerConnection> connectCallback;
	private Callback<ServerConnection> disconnectCallback;

	// singleton
	private Server(){}
	static Server server = new Server();
	static Server Get(){
		return server;
	}

	public void OnConnect(Callback<ServerConnection> connectCallback)
	{	
		this.connectCallback = connectCallback;
	}

	public void OnDisconnect(Callback<ServerConnection> disconnectCallback)
	{	
		this.disconnectCallback = disconnectCallback;
	}

	public void OnRequest(RequestCallback<Message, ServerConnection> requestCallback)
	{	
		this.requestCallback = requestCallback;
	}

	// called to remove client from the server
	public boolean RemoveClient(ServerConnection client){
		
		String address = client.GetAddress();
		return clients.Remove(address);

	}

	// send a message to all connected clients
	public void Emit(Message<?> msg){
		for(ServerConnection client : clients) client.Send(msg);
	}
	
	// start server
	public void Listen(int port){

		Server.mainFrame.setTitle("Server Listening on " + port);

		if(disconnectCallback == null){
			Log.Err("Disconnect callback is not set.");
			return;
		}
		if(requestCallback == null){
			Log.Err("Message callback is not set.");
			return;
		}

		try {

			this.port = port;
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			Log.Log("Listening on port " + port);

			// running infinite loop for getting
			// client request
			while (true) {

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
		catch(BindException e){
			e.printStackTrace();
			Log.Err("Port " + port + " is already in use. Please choose another port.");			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// main 
		private static int i = 0;
		
		static JFrame mainFrame = new JFrame();
		static JPanel mainPanel = new JPanel(new GridBagLayout());

		static JButton button = new JButton("Emit");
		static JTextArea textBox = new JTextArea(40,76);
		
		static JList clientList = new JList();
		static DefaultListModel clientListModel;

		static JTextField inputBox = new JTextField(64);

		public static void main(String[] args) throws ClassNotFoundException
		{
					
			JTextArea textBox = new JTextArea();
			Log.SetTextArea(textBox);

			JScrollPane scrollPane = new JScrollPane(textBox);
			scrollPane.setPreferredSize(new Dimension(800, 600));

			textBox.setEditable(false);	
			
			mainFrame.add(mainPanel);	
			mainPanel.setLayout(new GridBagLayout());

			clientListModel = new DefaultListModel();
			clientList.setModel(clientListModel);
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5); 

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0.9;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			mainPanel.add(scrollPane, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0.1;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;

			JScrollPane jListScrollPane = new JScrollPane(clientList);
			mainPanel.add(jListScrollPane, c);

			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.weightx = 0.8;
			c.weighty = 0.01;
			mainPanel.add(inputBox, c);

			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.2;
			c.weighty = 0.01;
			mainPanel.add(button, c);

			server = Server.Get();

			button.addActionListener(e -> {

				String text = inputBox.getText();
				inputBox.setText("");

				Message msg = new Message(MessageType.PING, MessageStatus.REQUEST, new PingRequest[] { new PingRequest(text) });
				server.Emit(msg);

				Log.Log("Emit message: " + text);
			
			});
			
			mainFrame.setVisible(true);
			mainFrame.setSize(1024, 768);
			mainFrame.setLocationRelativeTo(null);

			// set server callback Callbacktions

			// set callback for when Server receives a message
			server.OnRequest(
				(Message msg, ServerConnection client) -> {
					
					if(msg.getType() == MessageType.PING && msg.getStatus() == MessageStatus.RESPONSE){
						// received message: send response
						Log.Log("RESPONSE from " + client.GetAddress() + ": [" + msg.getType() + "]:\n" + (String)msg.toString());

						//client.Send(new Message<client.requests.PingRequest>(MessageType.PING, MessageStatus.SUCCESS, new PingRequest[] { new PingRequest("Pong " + (i++)) } ));
					}
				
				}
				
			);

			// set callback for when new client connects
			server.OnConnect(
				(ServerConnection client) -> {

					Log.Log("Client has connected: " + client.GetAddress());
					clientListModel.addElement(client.GetAddress());

				}
			);

			// set callback for when new client disconnects
			server.OnDisconnect(
				(ServerConnection client) -> {

					Log.Log("Client has disconnected: " + client.GetAddress());
					clientListModel.removeElement(client.GetAddress());
				
				}
			);

			// start server
			server.Listen(7777);
			
		}
	// end main

}
