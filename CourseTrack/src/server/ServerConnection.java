package server;

import global.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

// ServerConnection is a single client socket connection
public class ServerConnection implements Runnable {

    // client socket objects
    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    // thread loop condition
    private boolean run = true;

    // callback for when server receives message from this connection
    private RequestCallback<Message, ServerConnection> messageCallback;

    // callback for when client disconnects from server
    private Callback<ServerConnection> disconnectCallback;

    // reference to server object
    private Server server;

    // Constructor
    public ServerConnection(Socket socket, Server server, RequestCallback<Message,ServerConnection> messageCallback, Callback<ServerConnection> disconnectCallback)
    {
        this.server = server;
        this.socket = socket;
        this.messageCallback = messageCallback;
        this.disconnectCallback = disconnectCallback;
    }

    // return inet address of this client
    public String GetAddress(){
        return this.socket.getInetAddress().toString();
    }

    // send a message to just this client
    public void Send(Message msg){

        try {
            objectOutputStream.writeObject(msg);
            
        }
		catch (IOException e) {
			e.printStackTrace();
		}
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
            while(run){

                // scan for incoming messages
                Message<?> message = (Message<?>) objectInputStream.readObject(); // java.io.EOFException after first messages received

                // create copy received message with client info
                messageCallback.call(message, this);
            }
            
        }
        catch (ClassNotFoundException e) {
            Log.Err("Class not found: " + e.getMessage());
            e.printStackTrace();
        }
        catch (EOFException | SocketException e) {
            
            Log.Err(e);
            run = false;

            // call disconnect
            disconnectCallback.call(this); 
            // remove client object from server's list
            server.RemoveClient(this);

        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // close the socket
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}