package server;

import global.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import server.data.*;

// ServerConnection is a single client socket connection
public class ServerConnection implements Runnable {

    // client socket objects
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    // thread loop condition
    private boolean run = true;

    // callback for when server receives message from this connection
    private Callback_T_U<Message, ServerConnection> messageCallback;

    // callback for when client disconnects from server
    private Callback_T<ServerConnection> disconnectCallback;

    // reference to server object
    private Server server;

    // user logged in to this session
    // starts null and becomes defined when login occurs
    private User user;

    Timer timeout = new Timer();
  
    // Constructor
    public ServerConnection(Socket socket, Server server, Callback_T_U<Message, ServerConnection> messageCallback, Callback_T<ServerConnection> disconnectCallback)
    {
        this.server = server;
        this.socket = socket;
        this.messageCallback = messageCallback;
        this.disconnectCallback = disconnectCallback;
    }

    // return inet address of this client
    public String getAddress() {
        return this.socket.getInetAddress().toString();
    }

    public boolean isLoggedIn() {
        return user != null;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // send a message to just this client
    public void Send(Message msg) {
        Log.Msg("Sending msg " + msg.getTypeString());
        try {
            
            objectOutputStream.reset();
            objectOutputStream.writeObject(msg);
            objectOutputStream.flush();
            
        }
		catch (IOException e) {

			e.printStackTrace();
		
        }
    }

    public <TObjMessage extends Serializable> void sendMessage(MessageType type, MessageStatus status, TObjMessage obj) {
        Send(new Message<>(type, status, obj));
    }

    public void Hangup() {
        
        user = null;
        run = false;
        disconnectCallback.call(this); 
        server.RemoveClient(this);

        try {
            socket.close();
        } catch (Exception e) {

        }
    }

    public void Ban() {
        this.user.Ban();
    }

    public boolean validateAdmin() {
        return user instanceof Admin;
    }

    public boolean validateStudent() {
        return user instanceof Student;
    }

    // Run the connection socket loop
    public void run()
    {
        try {
            
            // open socket stream
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            // loop thread
            while(run) {

                // scan for incoming messages
                Message<?> message = (Message<?>) objectInputStream.readObject(); // java.io.EOFException after first messages received

                // create copy received message with client info
                messageCallback.call(message, this);
            
            }
        }
        catch (ClassNotFoundException e) {
            Log.Err("Class not found: " + e.getMessage());
            e.printStackTrace();
            Hangup();
        }
        catch(EOFException e ) {
            Log.Msg("Client closed connection");
            Hangup();
        }
        catch ( SocketException e) {
            Log.Msg("Client closed connection");
            e.printStackTrace();
            Hangup();
        } 
        catch (IOException e) {
            e.printStackTrace();
            Hangup();
        }
        finally {
            
        }
        
    }
}