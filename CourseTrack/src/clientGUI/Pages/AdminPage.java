package clientGUI.Pages;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import client.services.IAppGUIService;
import clientGUI.UIFramework.*;
import clientGUI.PageComponents.*;
import clientGUI.UIInformations.*;
import global.data.Course;
public class AdminPage {
    
    public AdminPage(nFrame frame, IAppGUIService guiService, HomePage homePage) {
        int sidebarX = homePage.sidebarX;
        int sidebarY = homePage.sidebarY;
        int sidebarWidth = homePage.sidebarWidth;
        int sidebarHeight = homePage.sidebarHeight;
        int mainX = homePage.mainX;
        int mainY = homePage.mainY;
        int mainW = homePage.mainW;
        int mainH = homePage.mainH;
        buildAdminHome(frame, guiService, sidebarX, sidebarY, sidebarWidth, sidebarHeight, mainX, mainY, mainW, mainH);
    }
    private void buildAdminHome(nFrame frame, IAppGUIService guiService, int sidebarX, int sidebarY, int sidebarWidth, int sidebarHeight, int mainX, int mainY, int mainW, int mainH) {

        UIArrayList<Component> navList = new UIArrayList<>();

        Color navBg = UITheme.BG_APP;
        nButton navManage = new nButton("Manage Courses");
        nButton navReports = new nButton("Generate Reports");
        

        navManage.setBackgroundColor(navBg);
        navReports.setBackgroundColor(navBg);

        navList.append(navManage);
        navList.append(navReports);

        Component[] navComponents = navList.toArray(new Component[navList.getLength()]);

        nFrame.ListLayout navLayout = new nFrame.ListLayout(frame,navComponents,new Dimension(sidebarWidth, sidebarHeight),sidebarX,sidebarY);
        navLayout.backgroundColor = UITheme.BG_ELEVATED2;
        navLayout.setPadding(6, 6);
        navLayout.setStyle(nFrame.ListLayout.Style.NONE);

        nFrame.ListLayout viewManage = PageViews.createManageView(frame, mainX, mainY, mainW, mainH, guiService, UserRole.admin);
        nFrame.ListLayout viewReports = PageViews.createReportsView(frame, mainX, mainY, mainW, mainH);
        Component[] views = {viewManage, viewReports};
        clearCenterView(views);

        viewManage.setVisible(true);
        

        navManage.addActionListener(e -> {clearCenterView(views);viewManage.setVisible(true);});
        navReports.addActionListener(e -> {clearCenterView(views);viewReports.setVisible(true);});

    }
    private void clearCenterView(Component[] views) {
        for(Component c : views) {c.setVisible(false);}
    }
    //#endregion
}
