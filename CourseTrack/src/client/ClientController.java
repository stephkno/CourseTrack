package client;

import client.services.IAppGUIService;
import client.services.IClientListenerService;
import clientGUI.ClientUIManager;
import clientGUI.UIInformations.LoginInformation;
import global.*;
import global.data.*;
import global.requests.*;
import global.responses.*;
import java.io.Serializable;

public class ClientController implements  IClientListenerService, IAppGUIService {
    private ClientListener clientListener;
    private final Client client;
    private User currentUser;

    ClientUIManager clientUI = new ClientUIManager();
    LoginInformation lInfo = new LoginInformation();

    public ClientController(Client client) {
        this.client = client;
        clientUI.GoLoginPage(lInfo, this);
    }

    public void start() {
        if (clientListener == null) {
            clientListener = new ClientListener(client, this);
            new Thread(clientListener).start();
        }
        
        clientUI.GoLoginPage(lInfo, this);
    }

    @Override
    public <TObjResponse extends Serializable, TObjRequest extends Serializable>
    Message<TObjResponse> sendAndWait(MessageType type, MessageStatus status, TObjRequest obj) {
        return client.sendAndWait(
            new Message<TObjRequest>(
                type,
                status,
                obj
            )
        );
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
            case SUCCESS:{
                if (resp.get() == null || resp.get().user() == null) {
                    clientUI.setLoginValidationMessage("Malformed login response.");
                    return false;
                }

                currentUser = resp.get().user();

                if (currentUser.getUserType() == UserType.ADMIN) {
                    clientUI.GoAdminPage(() -> {
                        //logout();
                    }, this);
                } else {
                    clientUI.GoStudentPage(() -> {
                        //logout();
                    }, this);
                }
                clientUI.setLabel("Welcome, " + currentUser.getUserName());

                return true;
            }

            case FAILURE:{
                clientUI.setLoginValidationMessage("Invalid username or password.");
                return false;
            }

            default:{
                clientUI.setLoginValidationMessage("Unexpected server response.");
                return false;
            }
        }
    }

    @Override
    public boolean sendRegisterRequest(String username, String password, UserType type) {
        Message<RegisterResponse> resp = client.sendAndWait(new Message(
            MessageType.USER_REGISTER, 
            MessageStatus.REQUEST, 
            new RegisterRequest(username, password, type)
        ));

        if (resp == null) {
            Log.Err("Null response from server.");
            clientUI.setLoginValidationMessage("Null response from server.");
            return false;
        }

        switch (resp.getStatus()) {
            case SUCCESS -> {
                if (resp.get() == null) {
                    clientUI.setLoginValidationMessage("Malformed registration response.");
                    return false;
                }

                clientUI.GoLoginPage(lInfo, this);

                Log.Msg("User registered successfully: " + resp.get());
                return true;

            }

            case FAILURE -> {
                Log.Err("Invalid username or password");
                clientUI.setLoginValidationMessage("Invalid username or password.");
                return false;
            }

            default -> {
                Log.Err("Unexpected server response");
                clientUI.setLoginValidationMessage("Unexpected server response.");
                return false;
            }
        }

    }

    @Override
    public boolean sendLogoutRequest() {
        Message<LogoutResponse> resp = client.sendAndWait(
            new Message<>(
                MessageType.USER_LOGOUT, 
                MessageStatus.REQUEST, 
                new LogoutRequest()
            )
        );

        if (resp == null) {
            clientUI.setLoginValidationMessage("No response from server.");
            return false;
        }

        switch (resp.getStatus()) {
            case SUCCESS -> {
                clientUI.GoLoginPage(lInfo, this);
                return true;
            }

            case FAILURE -> {
                return false;
            }

            default -> {
                return false;
            }
        }
    }


    @Override
    public LinkedList<Section> sendBrowseSectionRequest(String query, String campus, String department, Term term, int max_requests){
        Message<BrowseSectionResponse> resp = client.sendAndWait(
            new Message<>(
                MessageType.STUDENT_BROWSE_SECTION, 
                MessageStatus.REQUEST, 
                new BrowseSectionRequest(query, campus, department, term, max_requests)
            )
        );

        if (resp == null) {
            Log.Err("No response from server.");
            return null;
        }

        switch (resp.getStatus()) {
            case SUCCESS -> {
                if (resp.get() == null || resp.get().sections() == null) {
                    Log.Err("Malformed server response.");
                    return null;
                }

                return resp.get().sections();
            }

            case FAILURE -> {
                Log.Err("Failed to get sections");
                return null;
            }

            default -> {
                Log.Err("Unexpected server response.");
                return null;
            }
        }
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

        Log.Msg("Received PingRequest request: " + ((PingRequest)request.get()).message());
        client.sendResponse(new Message<>(MessageType.PING_REQUEST, MessageStatus.RESPONSE, null));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleServerMessage(Message<?> request, Class<? extends Serializable> tClass) {
        switch (request.getType()) {
            case CLIENT_UPDATE -> {
                receiveUpdateRequest((Message<UpdateRequest>) request);
                break;
            }
            case PING_REQUEST -> {
                receivePingRequest((Message<PingRequest>) request);
                break;
            }
            default -> {
                System.err.println("Unhandled message type: " + request.getType());
                break;
            }
        }
    }

    public User getCurrentUser() { return currentUser; }
}