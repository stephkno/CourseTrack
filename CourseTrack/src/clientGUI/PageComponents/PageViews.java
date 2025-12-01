package clientGUI.PageComponents;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import clientGUI.UIFramework.*;
import global.LinkedList;
import global.data.Campus;
import global.data.Course;
import global.data.Department;
import clientGUI.UIInformations.UserRole;

import global.Log;

@SuppressWarnings("unused")
public class PageViews {

    //#region createBrowseView
    public static nFrame.ListLayout createBrowseView(nFrame frame,int x, int y, int w, int h, Course[] courses, UserRole userRole) {
        // heading
        nPanelPlainText heading = new nPanelPlainText("Browse Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;

        // search controls
        nPanelTextBox searchBox = new nPanelTextBox();
        searchBox.textColor = UITheme.TEXT_PRIMARY;
        searchBox.backgroundColor = UITheme.BG_APP;
        searchBox.drawBorder = true;

        nButton searchButton = new nButton("Search");
        searchButton.setBackgroundColor(UITheme.INFO);

        // row with search box + button
        nPanel searchRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = getHeight();

                int buttonWidth = 90;
                int controlHeight = 28;

                int boxWidth = getWidth() - buttonWidth - padding * 3;
                int yMid = (h - controlHeight) / 2;

                searchBox.setBounds(padding,yMid,boxWidth,controlHeight);

                searchButton.setBounds(padding * 2 + boxWidth,yMid,buttonWidth,controlHeight);
            }
        };
        searchRow.setLayout(null);
        searchRow.setOpaque(false);
        searchRow.add(searchBox);
        searchRow.add(searchButton);

        // scrollable course list
        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);
        searchBrowseManageScrollableList(frame, courseList, "", courses, UserRole.student);

        searchButton.addActionListener(e -> {
            String query = searchBox.getText();
            searchBrowseManageScrollableList(frame, courseList, query, courses, UserRole.student);
        });

        // content panel inside the card: heading at top, searchRow, then list filling
        // rest
        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int w = getWidth();
                int h = getHeight();

                int headingHeight = 32;
                int searchRowHeight = 40;

                heading.setBounds(padding,padding,w - padding * 2,headingHeight);

                int searchY = padding + headingHeight + padding;
                searchRow.setBounds(padding,searchY,w - padding * 2,searchRowHeight);

                int listY = searchY + searchRowHeight + padding;
                int listH = h - listY - padding;
                if (listH < 40)
                    listH = 40;

                courseList.setBounds(padding,listY,w - padding * 2,listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(searchRow);
        content.add(courseList);

        Component[] comps = { content };
        
        nFrame.ListLayout layout = new nFrame.ListLayout(frame,comps,new Dimension(w, h),x,y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion


    //#region createDropView
    public static nFrame.ListLayout createDropView(nFrame frame, Course[] courses, int x, int y, int w, int h) {
        // heading
        nPanelPlainText heading = new nPanelPlainText("Drop Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;


        // scrollable course list
        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);
        searchDropScrollableList(frame, courseList, courses, UserRole.student);


        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int w = getWidth();
                int h = getHeight();

                int headingHeight = 32;
                int searchRowHeight = 40;

                heading.setBounds(padding,padding,w - padding * 2,headingHeight);

                int listY = padding + searchRowHeight + padding;
                int listH = h - listY - padding;
                if (listH < 40)
                    listH = 40;

                courseList.setBounds(padding,listY,w - padding * 2,listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(courseList);

        Component[] comps = { content };
        
        nFrame.ListLayout layout = new nFrame.ListLayout(frame,comps,new Dimension(w, h),x,y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion
    //#region createWaitlistView
    public static nFrame.ListLayout createWaitlistView(nFrame frame,int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("Drop Course");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps,new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion
    //#region createScheduleView
    public static nFrame.ListLayout createScheduleView(nFrame frame,int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("View Schedule");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps,new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
        /* 
        scheduleList = new nScrollableList();
        scheduleList.setInnerPadding(8);
        scheduleList.setItemSpacing(8);
        populateScheduleList(scheduleList); // initial (possibly empty)

        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int cw = getWidth();
                int ch = getHeight();

                int headingHeight = 32;

                heading.setBounds(
                        padding,
                        padding,
                        cw - padding * 2,
                        headingHeight);

                int listY = padding + headingHeight + padding;
                int listH = ch - listY - padding;
                if (listH < 40) listH = 40;

                scheduleList.setBounds(
                        padding,
                        listY,
                        cw - padding * 2,
                        listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(scheduleList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(
                frame,
                comps,
                new Dimension(w, h),
                x,
                y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;*/
    }
    //#endregion
    /*
    //#region createWaitlistView
    public static nFrame.ListLayout createWaitlistView(nFrame frame,
            int x, int y, int w, int h) {
        JPanelPlainText heading = new JPanelPlainText("Waitlist & Notices");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelPlainText desc = new JPanelPlainText(
                "Display courses where the student is waitlisted and any\n" +
                        "notifications when seats become available.");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(
                frame,
                comps,
                new Dimension(w, h),
                x,
                y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion
    */
    //#region createManageView
    public static nFrame.ListLayout createManageView(nFrame frame, int x, int y, int w, int h, Course[] courses, UserRole userRole) {
        nPanelPlainText heading = new nPanelPlainText("Manage Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;
        nPanelTextBox searchBox = new nPanelTextBox();
        searchBox.textColor = UITheme.TEXT_PRIMARY;
        searchBox.backgroundColor = UITheme.BG_APP;
        searchBox.drawBorder = true;

        nButton searchButton = new nButton("Search");
        searchButton.setBackgroundColor(UITheme.INFO);

        nButton addButton = new nButton("Add");
        addButton.setBackgroundColor(UITheme.SUCCESS);

        nPanelDropDown campusChoose = new nPanelDropDown();
        nPanelDropDown departmentChoose = new nPanelDropDown();
        nButton addCampusButton = new nButton("New Campus");
        addCampusButton.setBackgroundColor(UITheme.SUCCESS);
        nButton addDepartmentButton = new nButton("New Department");
        addDepartmentButton.setBackgroundColor(UITheme.SUCCESS);

        addCampusButton.addActionListener(e -> {//String name, int number, int units, Department department
            int ww = 420;
            int hh = 400;
            
            nButton enrollButton = new nButton("Save");
            enrollButton.setBackgroundColor(UITheme.SUCCESS);
            nButton cancelButton = new nButton("Cancel");
            cancelButton.setBackgroundColor(UITheme.FAIL);
            Component[] options = {
                enrollButton,
                cancelButton
            };
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
            nPanelTextBox campusTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            Component[] enrollPanel = {
                new nPanelPlainText("Add Campus", UITheme.TEXT_PRIMARY),
                new nPanelPlainText("Campus", UITheme.TEXT_PRIMARY),
                campusTB,
                lowerOptions
            };
            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame)frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                //#region ADDS CAMPUS HERE
                Campus.add(campusTB.getText());      
                
                modal.close();
            });
            cancelButton.addActionListener(ee -> {
                modal.close();
            });
        });


        addDepartmentButton.addActionListener(e -> {//String name, int number, int units, Department department
            int ww = 420;
            int hh = 400;
            
            nButton enrollButton = new nButton("Save");
            enrollButton.setBackgroundColor(UITheme.SUCCESS);
            nButton cancelButton = new nButton("Cancel");
            cancelButton.setBackgroundColor(UITheme.FAIL);
            Component[] options = {
                enrollButton,
                cancelButton
            };
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
            nPanelTextBox departmentTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            Component[] enrollPanel = {
                new nPanelPlainText("Add Department", UITheme.TEXT_PRIMARY),
                new nPanelPlainText("Department", UITheme.TEXT_PRIMARY),
                departmentTB,
                lowerOptions
            };
            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame)frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                //#region ADDS DEPARTMENT HERE
                String campusName = campusChoose.getSelected().getText();
                Campus campus = Campus.get(campusName);
                campus.addDepartment(departmentTB.getText());     
                
                modal.close();
            });
            cancelButton.addActionListener(ee -> {
                modal.close();
            });
        });


        nPanel controlRow = new nPanel() {
            @Override
            public void doLayout() {
                int buttonAmount = 2;
                int padding = 10;
                int h = getHeight();

                int buttonWidth = 80;
                int controlHeight = 28;

                int totalButtonsWidth = buttonWidth * buttonAmount + padding * (buttonAmount-1);
                int boxWidth = getWidth() - totalButtonsWidth - padding * buttonAmount;
                if (boxWidth < 80)
                    boxWidth = 80;

                int yMid = (h) / 2;

                searchBox.setBounds(padding,yMid,boxWidth,controlHeight);
                campusChoose.setBounds(padding, 0, (int)getWidth()/4 - padding, controlHeight);
                addCampusButton.setBounds(padding+getWidth()/4, 0, (int)getWidth()/4 - padding, controlHeight);
                departmentChoose.setBounds(padding+(int)getWidth()/2, 0, (int)getWidth()/4 - padding, controlHeight);
                addDepartmentButton.setBounds(padding+(int)(getWidth()*0.75), 0, (int)getWidth()/4 - padding, controlHeight);
                int bx = padding * 2 + boxWidth;
                searchButton.setBounds(bx, yMid, buttonWidth, controlHeight);
                addButton.setBounds(bx + buttonWidth + padding, yMid, buttonWidth, controlHeight);
            }
        };
        controlRow.setLayout(null);
        controlRow.setOpaque(false);
        controlRow.add(campusChoose);
        controlRow.add(departmentChoose);
        controlRow.add(addCampusButton);
        controlRow.add(addDepartmentButton);
        controlRow.add(searchBox);
        controlRow.add(searchButton);
        controlRow.add(addButton);
        

        // scrollable list of courses
        nScrollableList manageCourseList = new nScrollableList();
        manageCourseList.setInnerPadding(8);
        manageCourseList.setItemSpacing(8);
        searchBrowseManageScrollableList(frame, manageCourseList, searchBox.getText(), courses, userRole);

        // wire up buttons
        searchButton.addActionListener(e -> {
            searchBrowseManageScrollableList(frame, manageCourseList, searchBox.getText(), courses, userRole);
        });

        addButton.addActionListener(e -> {//String name, int number, int units, Department department
            int ww = 420;
            int hh = 400;
            
            nButton enrollButton = new nButton("Save");
            enrollButton.setBackgroundColor(UITheme.SUCCESS);
            nButton cancelButton = new nButton("Cancel");
            cancelButton.setBackgroundColor(UITheme.FAIL);
            Component[] options = {
                enrollButton,
                cancelButton
            };
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
            nPanelTextBox campusTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            nPanelTextBox departmentTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            nPanelTextBox courseTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            nPanelTextBox courseNumberTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            nPanelTextBox courseUnitsTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            Component[] enrollPanel = {
                new nPanelPlainText("Add Course", UITheme.TEXT_PRIMARY),
                new nPanelPlainText("Course", UITheme.TEXT_PRIMARY),
                courseTB,
                new nPanelPlainText("Course Number", UITheme.TEXT_PRIMARY),
                courseNumberTB,
                new nPanelPlainText("Course Units", UITheme.TEXT_PRIMARY),
                courseUnitsTB,
                lowerOptions
            };
            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame)frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                //#region ADDS COURSE HERE
                String campusName = campusChoose.getSelected().getText();
                String departmentName = departmentChoose.getSelected().getText();
                Campus campus = Campus.get(campusName);
                Department department = campus.getDepartment(departmentName);
                department.addCourse(new Course(courseTB.getText(), Integer.parseInt(courseNumberTB.getText()), Integer.parseInt(courseUnitsTB.getText()), department));
                
                modal.close();
            });
            cancelButton.addActionListener(ee -> {
                modal.close();
            });
        });
        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int cw = getWidth();
                int ch = getHeight();

                int headingHeight = 32;
                int rowHeight = 80;

                heading.setBounds(padding,padding,cw - padding * 2,headingHeight);

                int rowY = padding + headingHeight + padding;
                controlRow.setBounds(padding,rowY,cw - padding * 2,rowHeight);

                int listY = rowY + rowHeight + padding;
                int listH = ch - listY - padding;
                if (listH < 40)
                    listH = 40;

                manageCourseList.setBounds(padding,listY,cw - padding * 2,listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(controlRow);
        content.add(manageCourseList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h),x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion
    //#region createReportsView
    public static nFrame.ListLayout createReportsView(nFrame frame, int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("Generate Reports");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText( "Just a graph to show off the graphs while WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        double[] xAxis = { 1, 2, 3, 4, 5 };
        double[] yAxis = { 1, 6, 3, 7, 3 };
        nPanelGraphs.GraphData gd = new nPanelGraphs.GraphData(xAxis, yAxis);
        nPanelGraphs graph1 = new nPanelGraphs(nPanelGraphs.GraphTypes.BAR, gd, 10, 10);
        nPanelGraphs graph2 = new nPanelGraphs(nPanelGraphs.GraphTypes.LINE, gd, 10, 10);
        Component[] c = { graph1, graph2 };
        nFrame.GridLayout subLayout = new nFrame.GridLayout(frame, c, new Dimension(1, 1), 1, 1);

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, subLayout, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    //#endregion

    private static void searchBrowseManageScrollableList(nFrame frame, nScrollableList list, String query, Course[] courses, UserRole userRole) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        list.clearItems();

        //#region just some temp logic for search query
        LinkedList<Course> ll = new LinkedList<Course>();
        for (Course c : courses) {
            if (!q.isEmpty()) {
                String haystack = (c.getName()).toLowerCase();
                if (!haystack.contains(q))
                    continue;
            }
            ll.Push(c); 
        }
        //#endregion

        ll.forEach(c -> {
            list.addItem(new Panels.CourseItemPanel(c, userRole));
        });
        frame.resizeChildren();
        //printTree((Container) list, "root");
        //for debuging
    }
    private static void searchDropScrollableList(nFrame frame, nScrollableList list, Course[] courses, UserRole userRole) {
        String q = "".trim().toLowerCase();
        list.clearItems();
        
        //#region just some temp logic for search query
        LinkedList<Course> ll = new LinkedList<Course>();
        for (Course c : courses) {
            if (!q.isEmpty()) {
                String haystack = (c.getName()).toLowerCase();
                if (!haystack.contains(q))
                    continue;
            }
            ll.Push(c); 
        }
        //#endregion

        ll.forEach(c -> {
            list.addItem(new Panels.DropItemPanel(c, userRole));
        });
        frame.resizeChildren();
        //printTree((Container) list, "root");
        //for debuging
    }

    private static void printTree(Container c, String currText){
        Component[] children = c.getComponents();
        for(Component child : children) {
            Log.Msg(currText + "." + child.getName());
            if(child instanceof Container) {
                printTree((Container) child, currText + "." + child.getName());
                child.repaint();
            }
        }
    }
}
