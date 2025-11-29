package client.services;
import client.UserType;

public interface IAppGUIService {
    boolean sendLoginRequest(String username, String password);
    boolean sendRegisterRequest(String username, String password, UserType type);
}
