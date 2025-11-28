package client;

import client.services.IAppGUIService;
import client.services.IClientListenerService;
import client.services.ILoginGUIService;
import global.*;
import global.requests.LoginRequest;
import global.requests.PingRequest;
import global.requests.UpdateRequest;
import global.responses.LoginResponse;
import global.responses.UpdateResponse;
import java.io.Serializable;

public class ClientController implements ILoginGUIService, IClientListenerService, IAppGUIService {
    private ClientListener clientListener;
    private final Client client;
    private AppGUI appGUI;
    private User currentUser;

    public ClientController(Client client) {
        this.client = client;
    }

    public void start() {
        new LoginGUI(this)
            .start();

        if (clientListener == null) {
            clientListener = new ClientListener(client, this);
            new Thread(clientListener).start();
        }

        appGUI = new AppGUI(this);
        appGUI.start();
    }

    @Override
    public boolean sendLoginRequest(String username, String password) {
        Message<LoginResponse> resp = client.sendAndWait(
            new Message<>(
                MessageType.USER_LOGIN, 
                MessageStatus.REQUEST, 
                new LoginRequest(username, password)
            )
        );

        if (resp == null)
            return false;
    
        switch (resp.getStatus()) {
            case SUCCESS -> {
                if (resp.get() == null) {
                    System.err.println("Malformed login response.");
                    return false;
                }
                    
                currentUser = resp.get().user();

                return true;
            }
            case FAILURE -> {
                System.err.println("Login failed.");
                return false;
            }
            default -> {
                System.err.println("Unexpected response status: " + resp.getStatus());
                return false;
            }
        }
    }

    public void receiveUpdateRequest(Message<UpdateRequest> request) {
        if (request.get() == null)
            return;

        appGUI.updateData(request.get());
        client.sendResponse(new Message<UpdateResponse>(MessageType.CLIENT_UPDATE, MessageStatus.RESPONSE, null));
    }
    
    public void receivePingRequest(Message<PingRequest> request) {
         if (request.get() == null)
            return;

        System.out.println("Received PingRequest request: " + ((PingRequest)request.get()).message());
        client.sendResponse(new Message<>(MessageType.PING_REQUEST, MessageStatus.RESPONSE, null));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleServerMessage(Message<?> request, Class<? extends Serializable> tClass) {
        switch (request.getType()) {
            case CLIENT_UPDATE -> receiveUpdateRequest((Message<UpdateRequest>) request);
            case PING_REQUEST -> receivePingRequest((Message<PingRequest>) request);
            default -> System.err.println("Unhandled message type: " + request.getType());
        }
    }

    public User getCurrentUser() { return currentUser; }
}