package client;

import client.services.IAppGUIService;
import client.services.IClientListenerService;
import clientGUI.ClientUIManager;
import clientGUI.UIInformations.LoginInformation;
import global.*;
import global.requests.LoginRequest;
import global.requests.PingRequest;
import global.requests.UpdateRequest;
import global.responses.LoginResponse;
import global.responses.UpdateResponse;
import java.io.Serializable;

public class ClientController implements  IClientListenerService, IAppGUIService {
    private ClientListener clientListener;
    private final Client client;
    private User currentUser;

    static ClientUIManager clientUI = new ClientUIManager();
    static LoginInformation lInfo = new LoginInformation();

    public ClientController(Client client) {
        this.client = client;
    }

    public void start() {
        if (clientListener == null) {
            clientListener = new ClientListener(client, this);
            new Thread(clientListener).start();
        }
        
        clientUI.GoLoginPage(lInfo, this);
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

        if (resp == null) {
            clientUI.setLoginValidationMessage("No response from server.");
            return false;
        }

        switch (resp.getStatus()) {
            case SUCCESS -> {
                if (resp.get() == null || resp.get().user() == null) {
                    clientUI.setLoginValidationMessage("Malformed login response.");
                    return false;
                }

                currentUser = resp.get().user();

                if (currentUser.getUserType() == UserType.ADMIN) {
                    clientUI.GoAdminPage(() -> {
                        //logout();
                    });
                } else {
                    clientUI.GoStudentPage(() -> {
                        //logout();
                    });
                }

                return true;
            }

            case FAILURE -> {
                clientUI.setLoginValidationMessage("Invalid username or password.");
                return false;
            }

            default -> {
                clientUI.setLoginValidationMessage("Unexpected server response.");
                return false;
            }
        }
    }


    @Override
    public boolean sendRegisterRequest(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendRegisterRequest'");
    }

    public void receiveUpdateRequest(Message<UpdateRequest> request) {
        if (request.get() == null)
            return;

        // appGUI.updateData(request.get());
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