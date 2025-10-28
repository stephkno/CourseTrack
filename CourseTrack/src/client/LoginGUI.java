package client;

import client.services.ILoginGUIService;

public class LoginGUI {
    private final ILoginGUIService loginService;
    
    public LoginGUI(ILoginGUIService clientController) {
        loginService = clientController;
    }

    public void start() {
        
    }

    public void stop() {

    }

    public void attemptLogin(String username, String password) {
        if (loginService.sendLoginRequest(username, password)) {
            System.out.println("Login successful!");
            stop();
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }
}
