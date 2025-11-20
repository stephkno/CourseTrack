package Pages;

import java.awt.Component;
import java.awt.Dimension;

import UIFramework.*;
import UIInformations.LoginInformation;

public class LoginPage {
    LoginInformation loginInformation;
    private ButtonInterface b;
    public LoginPage(nFrame frame, LoginInformation _loginInformation, ButtonInterface loginButtonAction) {
        b = loginButtonAction;
        loginInformation = _loginInformation;
        loginPage(frame);
    }
    public void loginPage(nFrame frame) {
        

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

        nButton loginButton = new nButton("Login");
        loginButton.setBackgroundColor(UITheme.ACCENT);

        loginButton.addActionListener(e -> {
            loginInformation.username = usernameInput.getText();
            loginInformation.password = passwordInput.getText();
            b.run();
            loginInformation.password = "";
            passwordInput.setText("");
        });

        Component[] components = {
                userNamePrompt,
                usernameInput,
                passwordPrompt,
                passwordInput,
                loginButton
        };

        int width = 300;
        int height = 260;

        int x = (int) ((frame.getSize().getWidth() - width) / 2);
        int y = (int) ((frame.getSize().getHeight() - height) / 2);

        nFrame.ListLayout list = new nFrame.ListLayout(frame, components, new Dimension(width, height), x, y);
        list.setPadding(8, 8);
        list.setStyle(nFrame.ListLayout.Style.NONE);
        list.backgroundColor = UITheme.BG_ELEVATED2;

        frame.revalidate();
        frame.repaint();
    }
}
