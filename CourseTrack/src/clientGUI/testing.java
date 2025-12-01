package clientGUI;

import clientGUI.Pages.LoginPage;
import clientGUI.UIInformations.LoginInformation;
import global.data.Campus;
import global.data.Course;
import global.data.Department;
import clientGUI.UIFramework.*;

@SuppressWarnings("unused")
public class testing {
    static ClientUIManager clientUI = new ClientUIManager();
    static LoginInformation lInfo = new LoginInformation();
    static ButtonInterface logoutButton = new ButtonInterface() {
        @Override
        public void run() {
            clientUI.GoLoginPage(lInfo, loginButton, registerButton);
        }
    };

    // JUST FOR SOME EXAMPLE LOGIC
    static global.LinkedList<String[]> exampleDatabaseLoginInfo = new global.LinkedList<String[]>();

    static ButtonInterface loginButton = new ButtonInterface() {
        @Override
        public void run() {
            
            

            //Log.Msg(lInfo.password);
            //Log.Msg(lInfo.username);
            //// EXAMPLE LOGIC

            // this is where the client/server auth would go but I just made a quick and
            // dirty version for testing and example

            boolean didAuth = false;

            // #region just so I can do some testing

            if ("admin".equals(lInfo.username)) {
                //clientUI.GoAdminPage(logoutButton, );
            }
            if ("student".equals(lInfo.username)) {
                //clientUI.GoStudentPage(logoutButton);
            }
            // #endregion

            for (String loginInfo[] : exampleDatabaseLoginInfo) {
                if (lInfo.username.equals(loginInfo[0])) {// check user
                    if (lInfo.password.equals(loginInfo[1])) {// check password
                        // send to respective page based on user type
                        if (loginInfo[2] == "STUDENT") {
                           // clientUI.GoStudentPage(logoutButton);
                        } else if (loginInfo[2] == "ADMIN") {
                            //clientUI.GoAdminPage(logoutButton);
                        }
                        didAuth = true;
                        break;
                    }
                }
            }

            if (didAuth) {
                // clear the login message below the loginbutton if authentication worked
                clientUI.setLoginValidationMessage("");
            } else {
                // send a generic message if authentication failed
                clientUI.setLoginValidationMessage("Either the username or password provided are invalid!",
                        UITheme.FAIL);
            }
        }
    };

    static ButtonInterface registerButton = new ButtonInterface() {
        @Override
        public void run() {

            //// EXAMPLE LOGIC
            ///
            /// some more example logic for the register button

            // I think this would not be typically done with a linear search but its easy
            // for the example I guess
            boolean userExists = false;
            for (String loginInfo[] : exampleDatabaseLoginInfo) {
                if (lInfo.username.equals(loginInfo[0])) {// check user
                    userExists = true;
                    break;
                }
            }
            if (userExists) {
                clientUI.setLoginValidationMessage("A user with the same username already exists!", UITheme.FAIL);
            } else if (lInfo.password.equals(lInfo.confirmationPassword)) {
                // Log.Msg(lInfo.password);
                // Log.Msg(lInfo.confirmationPassword);
                String[] user = { lInfo.username.strip(), lInfo.password.strip(), "STUDENT" };
                exampleDatabaseLoginInfo.Push(user);
                // clientUI.GoLoginPage(lInfo, loginButton, registerButton);
                clientUI.setLoginValidationMessage("User created! Please login.", UITheme.SUCCESS);
            } else {
                clientUI.setLoginValidationMessage("The passwords do not match!", UITheme.FAIL);
            }

        }
    };

    public static void main(String[] args) {

        clientUI.GoLoginPage(lInfo, loginButton, registerButton);

    }
}
