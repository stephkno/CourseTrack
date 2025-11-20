package PageComponents;

import java.awt.Component;
import java.awt.Dimension;

import UIFramework.*;
import UIInformations.UICourseInfo;
import UIInformations.UserRole;
public class PageViews {

    //#region createBrowseView
    public static nFrame.ListLayout createBrowseView(nFrame frame,int x, int y, int w, int h, UICourseInfo[] courses, UserRole userRole) {
        // heading
        JPanelPlainText heading = new JPanelPlainText("Browse Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;

        // search controls
        JPanelTextBox searchBox = new JPanelTextBox();
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
        searchScrollableList(courseList, "", courses, UserRole.student);

        searchButton.addActionListener(e -> {
            String query = searchBox.getText();
            searchScrollableList(courseList, query, courses, UserRole.student);
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
    public static nFrame.ListLayout createDropView(nFrame frame,int x, int y, int w, int h) {
        JPanelPlainText heading = new JPanelPlainText("Drop Course");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelPlainText desc = new JPanelPlainText("WIP");
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
    //#region createWaitlistView
    public static nFrame.ListLayout createWaitlistView(nFrame frame,int x, int y, int w, int h) {
        JPanelPlainText heading = new JPanelPlainText("Drop Course");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelPlainText desc = new JPanelPlainText("WIP");
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
        JPanelPlainText heading = new JPanelPlainText("View Schedule");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelPlainText desc = new JPanelPlainText("WIP");
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
    public static nFrame.ListLayout createManageView(nFrame frame, int x, int y, int w, int h, UICourseInfo[] courses, UserRole userRole) {
        JPanelPlainText heading = new JPanelPlainText("Manage Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelTextBox searchBox = new JPanelTextBox();
        searchBox.textColor = UITheme.TEXT_PRIMARY;
        searchBox.backgroundColor = UITheme.BG_APP;
        searchBox.drawBorder = true;

        nButton searchButton = new nButton("Search");
        searchButton.setBackgroundColor(UITheme.INFO);

        nButton addButton = new nButton("Add");
        addButton.setBackgroundColor(UITheme.SUCCESS);


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

                int yMid = (h - controlHeight) / 2;

                searchBox.setBounds(padding,yMid,boxWidth,controlHeight);

                int bx = padding * 2 + boxWidth;
                searchButton.setBounds(bx, yMid, buttonWidth, controlHeight);
                addButton.setBounds(bx + buttonWidth + padding, yMid, buttonWidth, controlHeight);
            }
        };
        controlRow.setLayout(null);
        controlRow.setOpaque(false);
        controlRow.add(searchBox);
        controlRow.add(searchButton);
        controlRow.add(addButton);

        // scrollable list of courses
        nScrollableList manageCourseList = new nScrollableList();
        manageCourseList.setInnerPadding(8);
        manageCourseList.setItemSpacing(8);
        searchScrollableList(manageCourseList, searchBox.getText(), courses, userRole);

        // wire up buttons
        searchButton.addActionListener(e -> {
            searchScrollableList(manageCourseList, searchBox.getText(), courses, userRole);
        });

        addButton.addActionListener(e -> {
            UICourseInfo newCourse = new UICourseInfo("CS000", "New Course", "Instructor", "Time", "Location");
            //showCourseEditDialog(frame, newCourse, true); 
            //#region do this 
            //#endregion
        });
        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int cw = getWidth();
                int ch = getHeight();

                int headingHeight = 32;
                int rowHeight = 40;

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
        JPanelPlainText heading = new JPanelPlainText("Generate Reports");
        heading.textColor = UITheme.TEXT_PRIMARY;

        JPanelPlainText desc = new JPanelPlainText( "Just a graph to show off the graphs while WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        double[] xAxis = { 1, 2, 3, 4, 5 };
        double[] yAxis = { 1, 6, 3, 7, 3 };
        JPanelGraphs.GraphData gd = new JPanelGraphs.GraphData(xAxis, yAxis);
        JPanelGraphs graph1 = new JPanelGraphs(JPanelGraphs.GraphTypes.BAR, gd, 10, 10);
        JPanelGraphs graph2 = new JPanelGraphs(JPanelGraphs.GraphTypes.LINE, gd, 10, 10);
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
    private static void searchScrollableList(nScrollableList list, String query, UICourseInfo[] courses, UserRole userRole) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        list.clearItems();

        for (UICourseInfo c : courses) {
            if (!q.isEmpty()) {
                String haystack = (c.code + " " + c.title + " " + c.instructor).toLowerCase();
                if (!haystack.contains(q))
                    continue;
            }
            list.addItem(new Panels.CourseItemPanel(c, userRole));
        }
    }

}
