package server;

import client.requests.PingRequest;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import java.awt.*;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerApp {
    
	// main 
	static JFrame mainFrame = new JFrame();
	static JPanel mainPanel = new JPanel(new GridBagLayout());

	static JButton button = new JButton("Ping All");
	static JTextArea textBox = new JTextArea(40,76);
	
	static JList clientList = new JList();
	static DefaultListModel clientListModel;

	static JTextField inputBox = new JTextField(64);

	public static void main(String[] args) throws ClassNotFoundException {
		
		ServerController controller = ServerController.Get();

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

		Server server = Server.Get();
        mainFrame.setTitle("Server Listening on " + server.GetPort());

		button.addActionListener(e -> {

			String text = inputBox.getText();
			inputBox.setText("");

			Message msg = new Message(MessageType.PingRequest, MessageStatus.REQUEST, new PingRequest[] { new PingRequest(text) });
			server.Emit(msg);

			Log.Msg("Ping message: " + text);
		
		});
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setSize(1024, 768);
		mainFrame.setLocationRelativeTo(null);

		// set server callback callback functions

		// set callback for when Server receives a message
		server.OnRequest(
			(Message msg, ServerConnection client) -> {
				
				controller.HandleMessage(msg, client);
				
			}
			
		);

		// set callback for when new client connects
		server.OnConnect(
			(ServerConnection client) -> {

				Log.Msg("Client has connected: " + client.GetAddress());
				clientListModel.addElement(client.GetAddress());

			}
		);

		// set callback for when new client disconnects
		server.OnDisconnect(
			(ServerConnection client) -> {

				Log.Msg("Client has disconnected: " + client.GetAddress());
				clientListModel.removeElement(client.GetAddress());
			
			}
		);

		// start server
		server.Listen(7777);
		
	}
	// end main
}
