package clientGUI.Pages;

import client.services.IAppGUIService;
import clientGUI.UIFramework.*;
import clientGUI.UIInformations.LoginInformation;
import java.awt.Component;
import java.awt.Dimension;

public class LoginPage {
    LoginInformation loginInformation;
    private IAppGUIService guiService;
    LoginType type;
    private nFrame.ListLayout list;
    public JPanelPlainText message;
    public LoginPage(nFrame frame, LoginInformation _loginInformation, LoginType logintype, IAppGUIService guiService) {
        this.guiService = guiService;
        type = logintype;
        loginInformation = _loginInformation;
        loginPage(frame);
    }
    public void loginPage(nFrame frame) {
        

        nButton registerButton = new nButton(type == LoginType.LOGIN ? "Create An Account" : "I Already Have An Account");
        registerButton.setBackgroundColor(UITheme.ACCENT);

        registerButton.addActionListener(e -> {
            if(list != null) {
                type = type == LoginType.LOGIN ? LoginType.REGISTER : LoginType.LOGIN;
                loginInformation.username = "";
                loginInformation.password = "";
                loginInformation.confirmationPassword = "";
                frame.remove(list);
                loginPage(frame);
            } 
        });

        JPanelPlainText userNamePrompt = new JPanelPlainText("Username");
        userNamePrompt.textColor = UITheme.TEXT_PRIMARY;

        JPanelTextBox usernameInput = new JPanelTextBox();
        usernameInput.textColor = UITheme.TEXT_PRIMARY;
        usernameInput.backgroundColor = UITheme.BG_APP;
        usernameInput.drawBorder = true;

        JPanelPlainText passwordPrompt = new JPanelPlainText("Password");
        passwordPrompt.textColor = UITheme.TEXT_PRIMARY;

        JPanelTextBox passwordInput = new JPanelTextBox();
        passwordInput.textColor = UITheme.TEXT_PRIMARY;
        passwordInput.backgroundColor = UITheme.BG_APP;
        passwordInput.drawBorder = true;


        JPanelPlainText passwordPromptConfirm = new JPanelPlainText("Password");
        passwordPromptConfirm.textColor = UITheme.TEXT_PRIMARY;

        JPanelTextBox passwordInputConfirm = new JPanelTextBox();
        passwordInputConfirm.textColor = UITheme.TEXT_PRIMARY;
        passwordInputConfirm.backgroundColor = UITheme.BG_APP;
        passwordInputConfirm.drawBorder = true;

        nButton loginButton = new nButton(type == LoginType.LOGIN ? "Login" : "Register");
        loginButton.setBackgroundColor(UITheme.ACCENT);

        loginButton.addActionListener(e -> {
            loginInformation.username = usernameInput.getText();
            loginInformation.password = passwordInput.getText();
            loginInformation.confirmationPassword = passwordInputConfirm.getText();
            if(type == LoginType.LOGIN) { guiService.sendLoginRequest(loginInformation.username, loginInformation.password); }
            else { guiService.sendRegisterRequest(loginInformation.username, loginInformation.password); }
            
            loginInformation.password = "";
            loginInformation.confirmationPassword = "";
            passwordInput.setText("");
            passwordInputConfirm.setText("");
        });

        JPanelPlainText errorText = new JPanelPlainText("");
        errorText.textColor = UITheme.FAIL;

        message = errorText;

        Component[] components = type == LoginType.LOGIN ? new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                loginButton,
                errorText
        } :
        new Component[]{
                registerButton,
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                passwordPromptConfirm,
                passwordInputConfirm,
                loginButton,
                errorText
        };

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
