package client;

import java.io.Serializable;

import client.requests.LoginRequest;
import client.requests.UpdateRequest;
import client.responses.LoginResponse;
import client.responses.UpdateResponse;
import client.services.IAppGUIService;
import client.services.IClientListenerService;
import client.services.ILoginGUIService;

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
        client.sendRequest(new Message<>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new LoginRequest[]{new LoginRequest(username, password)}));
        Message<LoginResponse> resp = client.receiveResponse();

        if (resp == null)
            return false;
    
        switch (resp.getStatus()) {
            case SUCCESS -> {
                if (resp.getArguments().length <= 0) {
                    System.err.println("Malformed login response.");
                    return false;
                }
                    
                currentUser = resp.getArguments()[0].user();
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
        if (request.getArguments().length != 1)
            return;

        appGUI.updateData(request.getArguments()[0]);

        client.sendResponse(new Message<UpdateResponse>(MessageType.UPDATE, MessageStatus.SUCCESS, null));
    }

    // public void receivePingRequest(Message<PingRequest> request) {
    //     if (request.getArguments().length != 1)
    //         return;

    //     client.sendResponse(new Message<PingResponse>(MessageType.PING, MessageStatus.SUCCESS, null));
    // }

    @Override
    @SuppressWarnings("unchecked")
    public void handleServerMessage(Message<?> request, Class<? extends Serializable> tClass) {
        switch (request.getType()) {
            case UPDATE -> receiveUpdateRequest((Message<UpdateRequest>) request);
            // case PING -> receivePingRequest((Message<PingRequest>) request);
            default -> System.err.println("Unhandled message type: " + request.getType());
        }
    }

    public User getCurrentUser() { return currentUser; }
}