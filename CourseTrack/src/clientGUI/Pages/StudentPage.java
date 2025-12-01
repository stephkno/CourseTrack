package clientGUI.Pages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import client.services.IAppGUIService;
import clientGUI.PageComponents.PageViews;
import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;
import global.data.Course;

public class StudentPage {
    //#region buildStudentHome
    
    public StudentPage(nFrame frame, IAppGUIService guiService, HomePage homePage) {
        int sidebarX = homePage.sidebarX;
        int sidebarY = homePage.sidebarY;
        int sidebarWidth = homePage.sidebarWidth;
        int sidebarHeight = homePage.sidebarHeight;
        int mainX = homePage.mainX;
        int mainY = homePage.mainY;
        int mainW = homePage.mainW;
        int mainH = homePage.mainH;
        buildStudentHome(frame, UserRole.student, guiService, sidebarX, sidebarY, sidebarWidth, sidebarHeight, mainX, mainY, mainW, mainH);
    }
    public void buildStudentHome(nFrame frame, UserRole role, IAppGUIService guiService, int sidebarX, int sidebarY,int sidebarWidth, int sidebarHeight,int mainX, int mainY,int mainW, int mainH) {

        UIArrayList<Component> navList = new UIArrayList<>();

        Color navBg = UITheme.BG_APP;

        nButton navBrowse = new nButton("Browse Courses");
        nButton navDrop = new nButton("Drop Course");
        nButton navSchedule = new nButton("View Schedule");
        nButton navWaitlist = new nButton("Waitlist / Notices");

        navBrowse.setBackgroundColor(navBg);
        navDrop.setBackgroundColor(navBg);
        navSchedule.setBackgroundColor(navBg);
        navWaitlist.setBackgroundColor(navBg);

        navList.append(navBrowse);
        navList.append(navDrop);
        navList.append(navSchedule);
        navList.append(navWaitlist);

        Component[] navComponents = navList.toArray(new Component[navList.getLength()]);

        nFrame.ListLayout navLayout = new nFrame.ListLayout(
                frame,
                navComponents,
                new Dimension(sidebarWidth, sidebarHeight),
                sidebarX,
                sidebarY);
        navLayout.backgroundColor = UITheme.BG_ELEVATED2;
        navLayout.setPadding(6, 6);
        navLayout.setStyle(nFrame.ListLayout.Style.NONE);

        // center views
        nFrame.ListLayout viewBrowse = PageViews.createBrowseView(frame, mainX, mainY, mainW, mainH, guiService, role);
        nFrame.ListLayout viewDrop = PageViews.createDropView(frame, mainX, mainY, mainW, mainH, guiService);
        nFrame.ListLayout viewSchedule = PageViews.createScheduleView(frame, mainX, mainY, mainW, mainH);
        nFrame.ListLayout viewWaitlist = PageViews.createWaitlistView(frame, mainX, mainY, mainW, mainH);

        nPanel[] views = {
                viewBrowse,
                viewDrop,
                viewSchedule,
                viewWaitlist
        };
        clearCenterView(views);
        viewBrowse.setVisible(true);
        navBrowse.addActionListener(e -> {clearCenterView(views);viewBrowse.setVisible(true);});
        navDrop.addActionListener(e -> {clearCenterView(views);viewDrop.setVisible(true);});
        navSchedule.addActionListener(e -> {clearCenterView(views);viewSchedule.setVisible(true);});
        navWaitlist.addActionListener(e -> {clearCenterView(views);viewWaitlist.setVisible(true);});

    }
    
    
    
    
    private void clearCenterView(Component[] views) {
        for(Component c : views) {c.setVisible(false);}
    }
    //#endregion
}
