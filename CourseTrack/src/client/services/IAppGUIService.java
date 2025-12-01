package client.services;
import client.UserType;
import global.data.Section;
import global.data.Term;
import global.LinkedList;

public interface IAppGUIService {
    boolean sendLoginRequest(String username, String password);
    boolean sendRegisterRequest(String username, String password, UserType type);
    LinkedList<Section> sendBrowseSectionRequest(String query, String campus, String department, Term term, int max_requests);
}
