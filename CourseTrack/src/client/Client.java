package client;

import global.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String host;
    private final int port;
    private boolean connected;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(5000);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            connected = true;
            System.out.println("Connected to server " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            connected = false;
            if (socket != null) 
                socket.close();
            System.out.println("Disconnected.");
        } catch (IOException e) {
            System.out.println("Error during disconnection: " + e.getMessage());
        }
    }

    public <TObjMessage extends Serializable> void sendResponse(Message<TObjMessage> request) {
        sendRequest(request);
    }
    public <TObjMessage extends Serializable> void sendRequest(Message<TObjMessage> request) {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending request: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <TObjMessage extends Serializable> Message<TObjMessage> receiveResponse() {
        try {
            return (Message<TObjMessage>) inputStream.readObject();
        } catch (ClassNotFoundException e){
            System.err.println("Error: No such class " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
