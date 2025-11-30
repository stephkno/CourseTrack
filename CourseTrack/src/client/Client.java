package client;

import global.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String host;
    private final int port;
    private boolean connected;

    private final Map<MessageType, CompletableFuture<Message<?>>> pending = new ConcurrentHashMap<>();

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            connected = true;
            Log.Msg("Connected to server " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            connected = false;
            if (socket != null) {
                socket.close();
                socket = null;
            }
            Log.Msg("Disconnected.");
        } catch (IOException e) {
            Log.Msg("Error during disconnection: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <TObjRequest extends Serializable, TObjResponse extends Serializable>
    Message<TObjResponse> sendAndWait(Message<TObjRequest> request) {
        CompletableFuture<Message<?>> future = new CompletableFuture<>();
        pending.put(request.getType(), future);

        sendRequest(request);

        try {
            return (Message<TObjResponse>) future.get();
        } catch (InterruptedException | ExecutionException e) {
            pending.remove(request.getType());
            System.err.println("Error waiting for response: " + e.getMessage());
            return null;
        }
    }


    public void sendResponse(Message<?> response) {
        sendRequest(response);
    }
    public void sendRequest(Message<?> request) {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending request: " + e.getMessage());
        }
    }

    public boolean routeIncoming(Message<?> msg) {
        CompletableFuture<Message<?>> future = pending.remove(msg.getType());

        if (future != null) {
            future.complete(msg);
            return true;
        }
        return false;
    }

    Message<?> internalReceive() {
        try {
            return (Message<?>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
