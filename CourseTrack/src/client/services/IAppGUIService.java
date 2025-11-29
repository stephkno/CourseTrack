package client.services;

public interface IAppGUIService {
    boolean sendLoginRequest(String username, String password);
    boolean sendRegisterRequest(String username, String password);
}
