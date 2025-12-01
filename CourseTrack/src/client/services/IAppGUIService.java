package client.services;
import java.io.Serializable;

import client.UserType;
import global.data.Section;
import global.data.Term;
import global.LinkedList;
import global.MessageStatus;
import global.MessageType;
import global.Message;

public interface IAppGUIService {
    boolean sendLoginRequest(String username, String password);
    boolean sendRegisterRequest(String username, String password, UserType type);
    boolean sendLogoutRequest();
    public <TObjResponse extends Serializable, TObjRequest extends Serializable>
    Message<TObjResponse> sendAndWait(MessageType type, MessageStatus status, TObjRequest obj);

    LinkedList<Section> sendBrowseSectionRequest(String query, String campus, String department, Term term, int max_requests);
}
