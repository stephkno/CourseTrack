package clientGUI;

import java.awt.Color;
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
import global.LinkedList;

public class ClientUIManager {
    
    private final String title = "CourseTrack";
    private final int startingWidth = 500;
    private final int startingHeight = 500;
    nFrame frame;
    
    public static Campus campus = null;

    
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
    

    public void GoAdminPage(ButtonInterface logoutButtonAction, IAppGUIService guiService) {
        cleanUp();

        String title = "Welcome, ";
        
        HomePage hp = new HomePage(frame, logoutButtonAction, title);

        frame.getContentPane().setBackground(UITheme.BG_APP);
        
        new AdminPage(frame, guiService, hp);
        //public AdminPage(nFrame frame, UICourseInfo[] courses, int sidebarX, int sidebarY, int sidebarWidth, int sidebarHeight, int mainX, int mainY, int mainW, int mainH) {
    }

    public void GoStudentPage(ButtonInterface logoutButtonAction, IAppGUIService guiService){
        cleanUp();

        String title = "Welcome, ";

        HomePage hp = new HomePage(frame, logoutButtonAction, title);
        frame.getContentPane().setBackground(UITheme.BG_APP);


        

        // todo: add course requirements!
        Course[] myCourseInfo = {new Course("title", 1, 1, campus.getDepartment("Computer Science"), new LinkedList<Course>())};
        new StudentPage(frame, guiService, myCourseInfo, hp);
    }


    private void cleanUp() {
        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.getContentPane().setBackground(UITheme.BG_APP);
    }

}
