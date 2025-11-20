package clientGUI;

import clientGUI.Pages.LoginPage;
import clientGUI.UIFramework.ButtonInterface;
import clientGUI.UIInformations.LoginInformation;
import clientGUI.UIFramework.*;

public class testing {
    static ClientUIManager clientUI = new ClientUIManager();
    static LoginInformation lInfo = new LoginInformation();
    static ButtonInterface logoutButton = new ButtonInterface() {
        @Override
        public void run() {
            clientUI.GoLoginPage(lInfo, loginButton);
    }};
    static ButtonInterface loginButton = new ButtonInterface() {
        @Override
        public void run() {
            System.out.println(lInfo.password);
            System.out.println(lInfo.username);
            if(lInfo.username.equalsIgnoreCase("admin")) {
                clientUI.GoAdminPage(logoutButton);
            } else if(lInfo.username.equalsIgnoreCase("student")){
                clientUI.GoStudentPage(logoutButton);
            }
            
    }};
    public static void main(String[] args) {
        
        
        clientUI.GoLoginPage(lInfo, loginButton);

    }
}
