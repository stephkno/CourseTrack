
import Pages.LoginPage;
import UIFramework.ButtonInterface;
import UIInformations.LoginInformation;
import UIFramework.*;

public class testing {
    static ClientUI clientUI = new ClientUI();
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
