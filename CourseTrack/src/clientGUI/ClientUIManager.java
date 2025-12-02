package clientGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

import client.services.IAppGUIService;
import client.ClientController;

import clientGUI.Pages.*;
import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;

import global.data.Campus;
import global.data.Course;
import global.data.Department;

import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.LinkedList;

public class ClientUIManager {
    
    private final String title = "CourseTrack";
    private final int startingWidth = 500;
    private final int startingHeight = 500;
    nFrame frame;
    
    public static Campus campus = null;
    HomePage hp;

    public void setLabel(String text){
        if(hp == null) return;
        hp.setLabel(text);
    }
    
    public ClientUIManager(){
        frame = new nFrame(title, startingWidth, startingHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BG_APP);
        frame.setVisible(true);
    }

    LoginPage loginPage;
    public void GoLoginPage(LoginInformation loginInformation, IAppGUIService guiService) {
        cleanUp();
        loginPage = new LoginPage(frame, loginInformation, LoginPage.LoginType.LOGIN, guiService);
    }

    public void GoLoginPage(LoginInformation loginInformation, ButtonInterface loginButton, ButtonInterface registerButton) {
        cleanUp();
        loginPage = new LoginPage(frame, loginInformation, LoginPage.LoginType.LOGIN, loginButton, registerButton);
    }

    public void setLoginValidationMessage(String errString) {
        setLoginValidationMessage(errString, UITheme.FAIL);
    }

    public void setLoginValidationMessage(String errString, Color col) {

        Log.Msg(errString);
        if(loginPage != null && loginPage.message != null) {
            loginPage.message.setText(errString);
            loginPage.message.textColor = col;
        }
        
    }
/* 
    public void GoAdminPage(ButtonInterface logoutButtonAction) {
        cleanUp();

        String title = "Welcome, ";
        
        HomePage hp = new HomePage(frame, logoutButtonAction, title);

        frame.getContentPane().setBackground(UITheme.BG_APP);
        
        Course[] courseInfo = {};
        String campusCurrent = "CSUEB";
        if(!Campus.exists(campusCurrent)) {
            new AdminPage(frame, courseInfo, hp);
            return;
        }

        campus = Campus.get(campusCurrent);
        //I need to ask some questions about the campus object before i can make this work properly
        Department department = campus.getDepartment("Computer Science");
        courseInfo = new Course[department.length()];
        for(int i = 0; i < department.length(); i++) {
            courseInfo[i] = department.getCourse(i);
        }
        
        new AdminPage(frame, courseInfo, hp);
        //public AdminPage(nFrame frame, UICourseInfo[] courses, int sidebarX, int sidebarY, int sidebarWidth, int sidebarHeight, int mainX, int mainY, int mainW, int mainH) {
    }

    public void GoStudentPage(ButtonInterface logoutButtonAction){
        cleanUp();

        String title = "Welcome, ";

        HomePage hp = new HomePage(frame, logoutButtonAction, title);
        frame.getContentPane().setBackground(UITheme.BG_APP);

        campus = Campus.get("CSU East Bay");
        
        Course[] courseInfo = new Course[20];
        for(int i = 0; i < 20; i++) {
            courseInfo[i] = campus.getDepartment("CS").getCourse(i);
        }
        Course[] myCourseInfo = {new Course("title", 1, 1, campus.getDepartment("Computer Science"))};
        new StudentPage(frame, courseInfo, myCourseInfo, hp);
    }
    */
    

    public void GoAdminPage(IAppGUIService guiService) {
        cleanUp();

        String title = "";
        
        hp = new HomePage(frame, guiService, title);

        frame.getContentPane().setBackground(UITheme.BG_APP);
        
        new AdminPage(frame, guiService, hp);
        //public AdminPage(nFrame frame, UICourseInfo[] courses, int sidebarX, int sidebarY, int sidebarWidth, int sidebarHeight, int mainX, int mainY, int mainW, int mainH) {
    }

    public void GoStudentPage(IAppGUIService guiService){
        cleanUp();
        String title = "";

        hp = new HomePage(frame, guiService, title);
        frame.getContentPane().setBackground(UITheme.BG_APP);

        new StudentPage(frame, guiService, hp);
    }

    public nPanelModal DisplayNotification(String notificationText, Runnable onClose) {
        Container glass = (Container) frame.getGlassPane();
        Component[] existingModals = glass.getComponents();
        for(Component potentialModal : existingModals) {
            if(potentialModal instanceof nPanelModal) {
                nPanelModal modal = (nPanelModal) potentialModal;
                modal.close();
            }
        }
        int w = 420;
        int h = 280;

        nButton enrollButton = new nButton("OK");
        enrollButton.setBackgroundColor(UITheme.SUCCESS);
        Component[] options = {
            enrollButton
        };
        nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options, false);
        lowerOptions.setGridSize(1, 1);
        lowerOptions.setPadding(5);
        Component[] enrollPanel = {
            new nPanelPlainText("!NOTIFICATION!", UITheme.TEXT_PRIMARY),
            new nPanelPlainText(notificationText, UITheme.TEXT_PRIMARY),
            lowerOptions
        };
        nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
        panel.setPadding(5, 7);
        nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h, onClose);
        enrollButton.addActionListener(ee -> {
            modal.close();
        });
        return modal;
    }

    private void cleanUp() {
        frame.reset();
        frame.setLayout(null);
        frame.getContentPane().setBackground(UITheme.BG_APP);
        frame.revalidate();
        frame.repaint();
    }

}
