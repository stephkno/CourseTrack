package clientGUI.Pages;

import java.awt.Component;
import java.awt.Dimension;

import client.services.IAppGUIService;
import clientGUI.UIFramework.*;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.requests.*;
import global.responses.*;

@SuppressWarnings("unused")
public class HomePage {
    public int sidebarWidth;
    public int sidebarHeight;
    public int sidebarX;
    public int sidebarY;
    public int mainX;
    public int mainY;
    public int mainW;
    public int mainH;
    nPanelPlainText roleLabel;

    public HomePage(nFrame frame, IAppGUIService guiService, String headerRoleText) {
        homePage(frame, guiService, headerRoleText);
    }

    public void setLabel(String text){
        roleLabel.setText(text);
    }

    private void homePage(nFrame frame, IAppGUIService guiService, String headerRoleText) {
        
        int frameW = frame.getWidth();
        int frameH = frame.getHeight();
        int titleHeight = 80;
        sidebarWidth = 220;
        sidebarHeight = frameH - titleHeight - 40;
        sidebarX = 20;
        sidebarY = titleHeight + 20;
        mainX = sidebarX + sidebarWidth + 20;
        mainY = sidebarY;
        mainW = frameW - mainX - 20;
        mainH = sidebarHeight;
        nPanelPlainText title = new nPanelPlainText("courseTrack Home");
        title.setAlignment(nPanel.CrossStringAlignments.BOTTOM, nPanel.MainStringAlignments.LEFT);
        title.textColor = UITheme.TEXT_PRIMARY;

        String roleText = headerRoleText;
        roleLabel = new nPanelPlainText(roleText);
        roleLabel.setAlignment(nPanel.CrossStringAlignments.TOP, nPanel.MainStringAlignments.LEFT);
        roleLabel.textColor = UITheme.TEXT_MUTED;

        nButton logoutButton = new nButton("Logout");
        logoutButton.setBackgroundColor(UITheme.ACCENT);

        logoutButton.addActionListener(e -> {
            guiService.sendLogoutRequest();
        });

        nPanel bgPanel = new nPanel();

        nPanel headerPanel = new nPanel() {
            @Override
            public void doLayout() {
                int w = getWidth();
                int h = getHeight();
                int pad = 12;

                

                int titleHeight = 26;
                // title at top-left
                title.setBounds(pad,
                        0,
                        w,
                        50);

                // role text below title
                int roleHeight = 18;
                roleLabel.setBounds(pad,
                        0,
                        w,
                        50);

                // logout button at topright
                int btnWidth = 90;
                int btnHeight = 26;
                int bx = w - pad - btnWidth;
                int by = 4;
                logoutButton.setBounds(bx, by, btnWidth, btnHeight);


                bgPanel.setBounds(0, 0, w, 50+pad);
                bgPanel.setBackground(UITheme.BG_ELEVATED);
                bgPanel.setOpaque(true);
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(roleLabel);
        headerPanel.add(logoutButton);
        headerPanel.add(bgPanel);

        Component[] titleComponents = { headerPanel };

        
        nFrame.ListLayout titleLayout = new nFrame.ListLayout(frame,titleComponents,new Dimension(frameW, titleHeight),0,0);
        

        frame.revalidate();
        frame.repaint();
    }
}
