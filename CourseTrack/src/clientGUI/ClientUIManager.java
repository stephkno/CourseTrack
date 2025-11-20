package clientGUI;

import clientGUI.Pages.*;
import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class ClientUIManager {
    
    private final String title = "CourseTrack";
    private final int startingWidth = 500;
    private final int startingHeight = 500;
    nFrame frame;
    
    private static final List<UICourseInfo> COURSES = new ArrayList<>();
    static {
        COURSES.add(new UICourseInfo("CS101", "Introduction to Computer Science", "Dr. Lin", "MWF 10:00-10:50", "ENGR 201"));
        COURSES.add(new UICourseInfo("CS135", "Discrete Structures", "Dr. Patel", "TR 9:30-10:45", "SCI 110"));
        COURSES.add(new UICourseInfo("CS241", "Data Structures & Algorithms", "Dr. Alvarez", "MWF 13:00-13:50", "ENGR 305"));
        COURSES.add(new UICourseInfo("CS321", "Operating Systems", "Dr. Nguyen", "TR 11:00-12:15", "ENGR 415"));
        COURSES.add(new UICourseInfo("CS352", "Database Systems", "Dr. Kim", "MWF 14:00-14:50", "SCI 208"));
        COURSES.add(new UICourseInfo("MATH221", "Linear Algebra", "Dr. Brown", "TR 8:00-9:15", "MATH 120"));
        COURSES.add(new UICourseInfo("PHYS135", "Introductory Physics I", "Dr. Carter", "MWF 9:00-9:50", "PHY 101"));
        COURSES.add(new UICourseInfo("CS355", "Computer Networks", "Dr. Rossi", "TR 14:00-15:15", "ENGR 420"));
        COURSES.add(new UICourseInfo("CS399", "Special Topics: Game Development", "Dr. Miller", "MW 15:30-16:45", "LAB 12"));
    }

    public ClientUIManager(){
        frame = new nFrame(title, startingWidth, startingHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(UITheme.BG_APP);
        frame.setVisible(true);
    }

    public void GoLoginPage(LoginInformation loginInformation, ButtonInterface loginButtonAction) {
        cleanUp();
        new LoginPage(frame, loginInformation, loginButtonAction);
    }
    public void setLoginError(String errString) {
        try {
            
            nFrame.ListLayout l = (nFrame.ListLayout) frame.getChildren()[0].comp;
            JPanelPlainText errText = (JPanelPlainText) l.getComponents()[l.getComponentCount()-1];
            errText.setText(errString);
        } catch(Exception e) { e.printStackTrace(); }
        
    }
    public void GoAdminPage(ButtonInterface logoutButtonAction) {
        cleanUp();
        HomePage hp = new HomePage(frame, logoutButtonAction, "AdminTitle");
        frame.getContentPane().setBackground(UITheme.BG_APP);
        UICourseInfo[] courseInfo = COURSES.toArray(new UICourseInfo[COURSES.size()]);
        
        new AdminPage(frame, courseInfo, hp);
        //public AdminPage(nFrame frame, UICourseInfo[] courses, int sidebarX, int sidebarY, int sidebarWidth, int sidebarHeight, int mainX, int mainY, int mainW, int mainH) {
    }
    public void GoStudentPage(ButtonInterface logoutButtonAction){
        cleanUp();
        HomePage hp = new HomePage(frame, logoutButtonAction, "StudentTitle");
        frame.getContentPane().setBackground(UITheme.BG_APP);
        UICourseInfo[] courseInfo = COURSES.toArray(new UICourseInfo[COURSES.size()]);
        
        new StudentPage(frame, courseInfo, hp);
    }

    private void cleanUp() {
        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.getContentPane().setBackground(UITheme.BG_APP);
    }
}
