package clientGUI.Pages;

import client.services.IAppGUIService;
import clientGUI.UIFramework.*;
import clientGUI.UIInformations.LoginInformation;
import java.awt.Component;
import java.awt.Dimension;
import client.UserType;

public class LoginPage {
    LoginInformation loginInformation;
    private IAppGUIService guiService;
    LoginType type;
    private nFrame.ListLayout list;
    public nPanelPlainText message;
    public LoginPage(nFrame frame, LoginInformation _loginInformation, LoginType logintype, IAppGUIService guiService) {
        this.guiService = guiService;
        type = logintype;
        loginInformation = _loginInformation;
        loginPage(frame);
    }


    //This section is so I can do local testing

    private ButtonInterface loginButtonFunction;
    private ButtonInterface registerButtonFunction;
    public LoginPage(nFrame frame, LoginInformation _loginInformation, LoginType logintype, ButtonInterface loginButtonFunction, ButtonInterface registerButtonFunction) {
        this.loginButtonFunction = loginButtonFunction;
        this.registerButtonFunction = registerButtonFunction;
        type = logintype;
        loginInformation = _loginInformation;
        testLoginPage(frame);
    }
    private void loginPage(nFrame frame) {
        

        nButton registerButton = new nButton(type == LoginType.LOGIN ? "Create An Account" : "I Already Have An Account");
        registerButton.setBackgroundColor(UITheme.ACCENT);

        

        nPanelPlainText userNamePrompt = new nPanelPlainText("Username");
        userNamePrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox usernameInput = new nPanelTextBox();
        usernameInput.textColor = UITheme.TEXT_PRIMARY;
        usernameInput.backgroundColor = UITheme.BG_APP;
        usernameInput.drawBorder = true;

        nPanelPlainText passwordPrompt = new nPanelPlainText("Password");
        passwordPrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox passwordInput = new nPanelTextBox();
        passwordInput.textColor = UITheme.TEXT_PRIMARY;
        passwordInput.backgroundColor = UITheme.BG_APP;
        passwordInput.drawBorder = true;


        nPanelPlainText passwordPromptConfirm = new nPanelPlainText("Password");
        passwordPromptConfirm.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox passwordInputConfirm = new nPanelTextBox();
        passwordInputConfirm.textColor = UITheme.TEXT_PRIMARY;
        passwordInputConfirm.backgroundColor = UITheme.BG_APP;
        passwordInputConfirm.drawBorder = true;

        nPanelPlainText adminCheckBoxPrompt = new nPanelPlainText("Admin Registration?");
        adminCheckBoxPrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelCheckBox checkBox = new nPanelCheckBox();
        checkBox.setColor(UITheme.TEXT_PRIMARY);

        nButton loginButton = new nButton(type == LoginType.LOGIN ? "Login" : "Register");
        loginButton.setBackgroundColor(UITheme.ACCENT);

        loginButton.addActionListener(e -> {
            loginInformation.username = usernameInput.getText();
            loginInformation.password = passwordInput.getText();
            loginInformation.confirmationPassword = passwordInputConfirm.getText();

            // checkbox
            UserType userType = checkBox.getState() ? UserType.ADMIN : UserType.STUDENT;

            if(type == LoginType.LOGIN) { guiService.sendLoginRequest(loginInformation.username, loginInformation.password); }
            else { guiService.sendRegisterRequest(loginInformation.username, loginInformation.password, userType); }
            
            loginInformation.password = "";
            loginInformation.confirmationPassword = "";
            passwordInput.setText("");
            passwordInputConfirm.setText("");
        });
        System.out.println("BUILDING TEST");
        nPanelPlainText errorText = new nPanelPlainText("");
        errorText.textColor = UITheme.FAIL;

        message = errorText;

        Component[] loginComponents = new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                loginButton,
                errorText
        };

        Component[] registerComponents = new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                passwordPromptConfirm,
                passwordInputConfirm,
                adminCheckBoxPrompt,
                checkBox,
                loginButton,
                errorText
        };

        System.out.println(checkBox.getX() + " " + checkBox.getY() + " " + checkBox.getWidth() + " " + checkBox.getHeight());

        Component[] components = type == LoginType.LOGIN ? loginComponents : registerComponents;


        registerButton.addActionListener(e -> {
            if(list == null) { return; }
            type = type == LoginType.LOGIN ? LoginType.REGISTER : LoginType.LOGIN;
            loginInformation.username = "";
            loginInformation.password = "";
            loginInformation.confirmationPassword = "";

            Component[] newComponents = type == LoginType.LOGIN ? loginComponents : registerComponents;

            list.setChildren(newComponents);
        });


        int width = 300;
        int height = 260;

        int x = (int) ((frame.getSize().getWidth() - width) / 2);
        int y = (int) ((frame.getSize().getHeight() - height) / 2);

        list = new nFrame.ListLayout(frame, components, new Dimension(width, height), x, y);
        list.setPadding(8, 8);
        list.setStyle(nFrame.ListLayout.Style.NONE);
        list.backgroundColor = UITheme.BG_ELEVATED2;

        frame.revalidate();
        frame.repaint();
    }
    private void testLoginPage(nFrame frame) {
        

        nButton registerButton = new nButton(type == LoginType.LOGIN ? "Create An Account" : "I Already Have An Account");
        registerButton.setBackgroundColor(UITheme.ACCENT);

        

        nPanelPlainText userNamePrompt = new nPanelPlainText("Username");
        userNamePrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox usernameInput = new nPanelTextBox();
        usernameInput.textColor = UITheme.TEXT_PRIMARY;
        usernameInput.backgroundColor = UITheme.BG_APP;
        usernameInput.drawBorder = true;

        nPanelPlainText passwordPrompt = new nPanelPlainText("Password");
        passwordPrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox passwordInput = new nPanelTextBox();
        passwordInput.textColor = UITheme.TEXT_PRIMARY;
        passwordInput.backgroundColor = UITheme.BG_APP;
        passwordInput.drawBorder = true;


        nPanelPlainText passwordPromptConfirm = new nPanelPlainText("Password");
        passwordPromptConfirm.textColor = UITheme.TEXT_PRIMARY;

        nPanelTextBox passwordInputConfirm = new nPanelTextBox();
        passwordInputConfirm.textColor = UITheme.TEXT_PRIMARY;
        passwordInputConfirm.backgroundColor = UITheme.BG_APP;
        passwordInputConfirm.drawBorder = true;

        nPanelPlainText adminCheckBoxPrompt = new nPanelPlainText("Admin Registration?");
        adminCheckBoxPrompt.textColor = UITheme.TEXT_PRIMARY;

        nPanelCheckBox checkBox = new nPanelCheckBox();
        checkBox.setColor(UITheme.TEXT_PRIMARY);

        nButton loginButton = new nButton(type == LoginType.LOGIN ? "Login" : "Register");
        loginButton.setBackgroundColor(UITheme.ACCENT);

        loginButton.addActionListener(e -> {
            loginInformation.username = usernameInput.getText();
            loginInformation.password = passwordInput.getText();
            loginInformation.confirmationPassword = passwordInputConfirm.getText();

            // checkbox
            UserType userType = checkBox.getState() ? UserType.ADMIN : UserType.STUDENT;

            if(type == LoginType.LOGIN) { loginButtonFunction.run(); }
            else { registerButtonFunction.run(); }
            
            loginInformation.password = "";
            loginInformation.confirmationPassword = "";
            passwordInput.setText("");
            passwordInputConfirm.setText("");
        });
        System.out.println("BUILDING TEST");
        nPanelPlainText errorText = new nPanelPlainText("");
        errorText.textColor = UITheme.FAIL;

        message = errorText;

        Component[] loginComponents = new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                loginButton,
                errorText
        };

        Component[] registerComponents = new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                passwordPromptConfirm,
                passwordInputConfirm,
                adminCheckBoxPrompt,
                checkBox,
                loginButton,
                errorText
        };

        System.out.println(checkBox.getX() + " " + checkBox.getY() + " " + checkBox.getWidth() + " " + checkBox.getHeight());

        Component[] components = type == LoginType.LOGIN ? loginComponents : registerComponents;


        registerButton.addActionListener(e -> {
            if(list == null) { return; }
            type = type == LoginType.LOGIN ? LoginType.REGISTER : LoginType.LOGIN;
            loginInformation.username = "";
            loginInformation.password = "";
            loginInformation.confirmationPassword = "";

            Component[] newComponents = type == LoginType.LOGIN ? loginComponents : registerComponents;

            list.setChildren(newComponents);
        });


        int width = 300;
        int height = 260;

        int x = (int) ((frame.getSize().getWidth() - width) / 2);
        int y = (int) ((frame.getSize().getHeight() - height) / 2);

        list = new nFrame.ListLayout(frame, components, new Dimension(width, height), x, y);
        list.setPadding(8, 8);
        list.setStyle(nFrame.ListLayout.Style.NONE);
        list.backgroundColor = UITheme.BG_ELEVATED2;

        frame.revalidate();
        frame.repaint();
    }


    public enum LoginType {
        LOGIN,
        REGISTER
    }
}
