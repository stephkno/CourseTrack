package clientGUI.PageComponents;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import client.services.IAppGUIService;
import clientGUI.UIFramework.*;
import global.LinkedList;
import global.data.Campus;
import global.data.Course;
import global.data.Department;
import global.data.Section;
import global.data.Term;
import global.requests.*;
import global.requests.AdminRequests.*;
import global.responses.*;
import global.responses.AdminResponses.*;
import global.responses.StudentResponses.StudentGetScheduleResponse;
import global.responses.StudentResponses.StudentGetUnitResponse;
import server.data.Admin;
import clientGUI.UIInformations.UserRole;

import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;

@SuppressWarnings("unused")
public class PageViews {

    // #region createBrowseView
    public static nFrame.ListLayout createBrowseView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService,
            UserRole userRole) {
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

                searchBox.setBounds(padding, yMid, boxWidth, controlHeight);

                searchButton.setBounds(padding * 2 + boxWidth, yMid, buttonWidth, controlHeight);
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
        
        String tempcampusname = "CSU East Bay";
        /*searchButton.addActionListener(e -> {
            courseList.clearItems();
            String query = searchBox.getText();
            Message<StudentGetScheduleResponse> getCoursesResponse = guiService.sendAndWait(
                    MessageType.STUDENT_BROWSE_SECTION, MessageStatus.REQUEST, new StudentGetUnitResponse());
            LinkedList<Course> courses = getCoursesResponse.get().courses();

            Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
            LinkedList<Term> terms = getTermsResponse.get().terms();
            for (Course c : courses) {

                if (!c.getCampus().getName().equals(campusChoose.getSelected().getText())) {
                    continue;
                }
                if (!c.getDepartment().getName().equals(departmentChoose.getSelected().getText())) {
                    continue;
                }
                if (c.getName().contains(query)) {
                    for(Term t : terms) {
                        if(!t.getDisplayName().equals(termChoose.getSelected().getText())) {continue;}
                        courseList.addItem(new Panels.CourseItemPanel(c, userRole, t, guiService));
                    }
                    
                }
            }

            courseList.revalidate();
            courseList.repaint();
        });*/

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

                heading.setBounds(padding, padding, w - padding * 2, headingHeight);

                int searchY = padding + headingHeight + padding;
                searchRow.setBounds(padding, searchY, w - padding * 2, searchRowHeight);

                int listY = searchY + searchRowHeight + padding;
                int listH = h - listY - padding;
                if (listH < 40)
                    listH = 40;

                courseList.setBounds(padding, listY, w - padding * 2, listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(searchRow);
        content.add(courseList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    // #endregion

    // #region createDropView
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

                heading.setBounds(padding, padding, w - padding * 2, headingHeight);

                int listY = padding + searchRowHeight + padding;
                int listH = h - listY - padding;
                if (listH < 40)
                    listH = 40;

                courseList.setBounds(padding, listY, w - padding * 2, listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(courseList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }

    // #endregion
    // #region createWaitlistView
    public static nFrame.ListLayout createWaitlistView(nFrame frame, int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("Drop Course");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }

    // #endregion
    // #region createScheduleView
    public static nFrame.ListLayout createScheduleView(nFrame frame, int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("View Schedule");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("WIP");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanel spacer = new nPanel();
        spacer.setOpaque(false);

        Component[] comps = { heading, desc, spacer };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
        /*
         * scheduleList = new nScrollableList();
         * scheduleList.setInnerPadding(8);
         * scheduleList.setItemSpacing(8);
         * populateScheduleList(scheduleList); // initial (possibly empty)
         * 
         * nPanel content = new nPanel() {
         * 
         * @Override
         * public void doLayout() {
         * int padding = 10;
         * int cw = getWidth();
         * int ch = getHeight();
         * 
         * int headingHeight = 32;
         * 
         * heading.setBounds(
         * padding,
         * padding,
         * cw - padding * 2,
         * headingHeight);
         * 
         * int listY = padding + headingHeight + padding;
         * int listH = ch - listY - padding;
         * if (listH < 40) listH = 40;
         * 
         * scheduleList.setBounds(
         * padding,
         * listY,
         * cw - padding * 2,
         * listH);
         * }
         * };
         * content.setLayout(null);
         * content.setOpaque(false);
         * content.add(heading);
         * content.add(scheduleList);
         * 
         * Component[] comps = { content };
         * 
         * nFrame.ListLayout layout = new nFrame.ListLayout(
         * frame,
         * comps,
         * new Dimension(w, h),
         * x,
         * y);
         * layout.backgroundColor = UITheme.BG_ELEVATED2;
         * layout.setPadding(10, 10);
         * layout.setStyle(nFrame.ListLayout.Style.NONE);
         * return layout;
         */
    }

    // #endregion
    /*
     * //#region createWaitlistView
     * public static nFrame.ListLayout createWaitlistView(nFrame frame,
     * int x, int y, int w, int h) {
     * JPanelPlainText heading = new JPanelPlainText("Waitlist & Notices");
     * heading.textColor = UITheme.TEXT_PRIMARY;
     * 
     * JPanelPlainText desc = new JPanelPlainText(
     * "Display courses where the student is waitlisted and any\n" +
     * "notifications when seats become available.");
     * desc.textColor = UITheme.TEXT_MUTED;
     * 
     * nPanel spacer = new nPanel();
     * spacer.setOpaque(false);
     * 
     * Component[] comps = { heading, desc, spacer };
     * 
     * nFrame.ListLayout layout = new nFrame.ListLayout(
     * frame,
     * comps,
     * new Dimension(w, h),
     * x,
     * y);
     * layout.backgroundColor = UITheme.BG_ELEVATED2;
     * layout.setPadding(10, 10);
     * layout.setStyle(nFrame.ListLayout.Style.NONE);
     * return layout;
     * }
     * //#endregion
     */
    // #region createManageView
    public static nFrame.ListLayout createManageView(nFrame frame, int x, int y, int w, int h,
            IAppGUIService guiService, UserRole userRole) {

        Message<AdminGetCampusesResponse> response = guiService.sendAndWait(MessageType.ADMIN_GET_CAMPUSES,
                MessageStatus.REQUEST, new AdminGetCampusesRequest());
        LinkedList<Campus> campuses = response.get().campuses();

        nPanelPlainText heading = new nPanelPlainText("Manage Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;
        nPanelTextBox searchBox = new nPanelTextBox();
        searchBox.textColor = UITheme.TEXT_PRIMARY;
        searchBox.backgroundColor = UITheme.BG_APP;
        searchBox.drawBorder = true;

        nButton searchButton = new nButton("Search");
        searchButton.setBackgroundColor(UITheme.INFO);

        nButton addButton = new nButton("Create Course");
        addButton.setBackgroundColor(UITheme.SUCCESS);

        nPanelDropDown campusChoose = new nPanelDropDown();
        nPanelDropDown departmentChoose = new nPanelDropDown();
        nPanelDropDown termChoose = new nPanelDropDown();

        nScrollableList manageCourseList = new nScrollableList();
        manageCourseList.setInnerPadding(8);
        manageCourseList.setItemSpacing(8);

        searchButton.addActionListener(e -> {
            manageCourseList.clearItems();
            String query = searchBox.getText();
            Message<AdminGetCoursesResponse> getCoursesResponse = guiService.sendAndWait(
                    MessageType.ADMIN_GET_COURSES, MessageStatus.REQUEST, new AdminGetCoursesRequest());
            LinkedList<Course> courses = getCoursesResponse.get().courses();

            Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
            LinkedList<Term> terms = getTermsResponse.get().terms();
            for (Course c : courses) {
                if (c == null || campusChoose.getSelected() == null || departmentChoose.getSelected() == null) {
                    continue;
                }
                if (!c.getCampus().getName().equals(campusChoose.getSelected().getText())) {
                    continue;
                }
                if (!c.getDepartment().getName().equals(departmentChoose.getSelected().getText())) {
                    continue;
                }
                if (c.getName().contains(query)) {
                    for(Term t : terms) {
                        if(!t.getDisplayName().equals(termChoose.getSelected().getText())) {continue;}
                        manageCourseList.addItem(new Panels.CourseItemPanel(c, userRole, t, guiService));
                    }
                    
                }
            }

            manageCourseList.revalidate();
            manageCourseList.repaint();
        });

        for (Campus campus : campuses) {
            nButton b = new nButton();
            b.setText(campus.getName());
            campusChoose.addOption(b);
            b.addActionListener(e -> {
                manageCourseList.clearItems();
                departmentChoose.clearOptions();
                termChoose.clearOptions();

                Message<AdminGetDepartmentsResponse> getDepartmentsResponse = guiService.sendAndWait(
                        MessageType.ADMIN_GET_DEPARTMENTS, MessageStatus.REQUEST, new AdminGetDepartmentsRequest());

                LinkedList<Department> departments = getDepartmentsResponse.get().departments();
                for (Department department : departments) {
                    if (department == null) {
                        continue;
                    }
                    if (!department.getCampus().getName().equals(campus.getName())) {
                        continue;
                    }
                    nButton db = new nButton();
                    db.setText(department.getName());

                    departmentChoose.addOption(db);
                    db.addActionListener(ee -> {
                        termChoose.clearOptions();

                        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                                MessageStatus.REQUEST, new GetTermsRequest());
                        LinkedList<Term> terms = getTermsResponse.get().terms();
                        for (Term t : terms) {
                            nButton tb = new nButton();
                            tb.setText(t.getDisplayName());
                            termChoose.addOption(tb);
                        }

                        searchButton.simulateClick();

                        departmentChoose.revalidate();
                        departmentChoose.repaint();
                    });
                    if (departmentChoose.getSelected().getText().equals(department.getName())) {
                        db.simulateClick();
                    }

                }

                manageCourseList.revalidate();
                departmentChoose.revalidate();
                campusChoose.revalidate();
                manageCourseList.repaint();
                departmentChoose.repaint();
                campusChoose.repaint();
            });

            nButton selected = campusChoose.getSelected();
            if (selected != null && campus.getName().equals(selected.getText())) {
                b.simulateClick();
            }

        }

        nButton addCampusButton = new nButton("New Campus");
        addCampusButton.setBackgroundColor(UITheme.SUCCESS);
        nButton addDepartmentButton = new nButton("New Department");
        addDepartmentButton.setBackgroundColor(UITheme.SUCCESS);

        addCampusButton.addActionListener(e -> {// String name, int number, int units, Department department
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
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame) frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
            nPanelTextBox campusTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            Component[] enrollPanel = {
                    new nPanelPlainText("Add Campus", UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Campus", UITheme.TEXT_PRIMARY),
                    campusTB,
                    lowerOptions
            };

            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame) frame, enrollPanel, new Dimension(100, 100), 10,
                    10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame) frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                // #region ADDS CAMPUS HERE
                Message<AddCampusResponse> addCampusResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_CAMPUS,
                        MessageStatus.REQUEST, new AddCampusRequest(campusTB.getText()));
                Campus campus = addCampusResponse.get().campus();
                if (addCampusResponse.getStatus() == MessageStatus.SUCCESS) {
                    nButton b = new nButton();
                    b.setText(campusTB.getText());
                    campusChoose.addOption(b);

                    b.addActionListener(eee -> {
                        manageCourseList.clearItems();
                        departmentChoose.clearOptions();
                        termChoose.clearOptions();

                        Message<AdminGetDepartmentsResponse> getDepartmentsResponse = guiService.sendAndWait(
                                MessageType.ADMIN_GET_DEPARTMENTS, MessageStatus.REQUEST,
                                new AdminGetDepartmentsRequest());

                        LinkedList<Department> departments = getDepartmentsResponse.get().departments();
                        for (Department department : departments) {
                            if (department == null) {
                                continue;
                            }
                            if (!department.getCampus().getName().equals(campus.getName())) {
                                continue;
                            }
                            nButton db = new nButton();
                            db.setText(department.getName());

                            departmentChoose.addOption(db);
                            db.addActionListener(eeee -> {
                                manageCourseList.clearItems();
                                termChoose.clearOptions();
                                Message<AdminGetCoursesResponse> getCoursesResponse = guiService.sendAndWait(
                                        MessageType.ADMIN_GET_COURSES, MessageStatus.REQUEST,
                                        new AdminGetCoursesRequest());
                                LinkedList<Course> courses = getCoursesResponse.get().courses();
                                Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(
                                        MessageType.GET_TERMS, MessageStatus.REQUEST, new GetTermsRequest());
                                LinkedList<Term> terms = getTermsResponse.get().terms();
                                for (Term t : terms) {
                                    nButton tb = new nButton();
                                    tb.setText(t.getDisplayName());
                                    termChoose.addOption(tb);
                                }
                                for (Course course : courses) {

                                    if (!course.getDepartment().getName().equals(department.getName())) {
                                        continue;
                                    }
                                    manageCourseList.addItem(new Panels.CourseItemPanel(course, userRole));
                                    System.out.println(course.getName());
                                }
                                manageCourseList.revalidate();
                                manageCourseList.repaint();
                                departmentChoose.revalidate();
                                departmentChoose.repaint();
                            });
                            if (departmentChoose.getSelected().getText().equals(department.getName())) {
                                db.simulateClick();
                            }

                        }

                        manageCourseList.revalidate();
                        departmentChoose.revalidate();
                        campusChoose.revalidate();
                        manageCourseList.repaint();
                        departmentChoose.repaint();
                        campusChoose.repaint();
                    });

                    modal.close();
                }

            });
            cancelButton.addActionListener(ee -> {
                modal.close();
            });
        });

        addDepartmentButton.addActionListener(e -> {// String name, int number, int units, Department department
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
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame) frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
            nPanelTextBox departmentTB = new nPanelTextBox(UITheme.TEXT_PRIMARY);
            Component[] enrollPanel = {
                    new nPanelPlainText("Add Department", UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Department", UITheme.TEXT_PRIMARY),
                    departmentTB,
                    lowerOptions
            };
            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame) frame, enrollPanel, new Dimension(100, 100), 10,
                    10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame) frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                // #region ADDS DEPARTMENT HERE

                Message<AddDepartmentResponse> addCampusResponse = guiService.sendAndWait(
                        MessageType.ADMIN_ADD_DEPARTMENT, MessageStatus.REQUEST,
                        new AddDepartmentRequest(campusChoose.getSelected().getText(), departmentTB.getText()));
                if (addCampusResponse.getStatus() == MessageStatus.SUCCESS) {
                    nButton b = new nButton();
                    b.setText(departmentTB.getText());
                    departmentChoose.addOption(b);
                    b.addActionListener(eee -> {
                        termChoose.clearOptions();

                        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                                MessageStatus.REQUEST, new GetTermsRequest());
                        LinkedList<Term> terms = getTermsResponse.get().terms();
                        for (Term t : terms) {
                            nButton tb = new nButton();
                            tb.setText(t.getDisplayName());
                            termChoose.addOption(tb);
                        }

                        searchButton.simulateClick();

                        departmentChoose.revalidate();
                        departmentChoose.repaint();
                    });
                    modal.close();
                }
            });
            cancelButton.addActionListener(ee -> {
                modal.close();
            });
        });

        nPanel controlRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = getHeight();
                int w = getWidth();
                int buttonWidth = 80;
                int controlHeight = 28;

                int boxWidth = w - buttonWidth - padding * 2;
                if (boxWidth < 80)
                    boxWidth = 80;

                int ySec = (int) (h * 0.33);
                int yThir = (int) (h * 0.66);

                campusChoose.setBounds(padding, 0, (int) w / 4 - padding, controlHeight);
                addCampusButton.setBounds(padding + w / 4, 0, (int) w / 4 - padding, controlHeight);
                departmentChoose.setBounds(padding + (int) w / 2, 0, (int) w / 4 - padding, controlHeight);
                addDepartmentButton.setBounds(padding + (int) (w * 0.75), 0, (int) w / 4 - padding, controlHeight);

                int bx = padding * 2 + boxWidth;
                addButton.setBounds(w / 2 + padding, ySec, w / 2 - padding, controlHeight);
                termChoose.setBounds(padding, ySec, w / 2 - padding, controlHeight);

                searchBox.setBounds(padding, yThir, boxWidth, controlHeight);
                searchButton.setBounds(bx, yThir, buttonWidth, controlHeight);

            }
        };
        controlRow.setLayout(null);
        controlRow.setOpaque(false);
        controlRow.add(campusChoose);
        controlRow.add(departmentChoose);
        controlRow.add(addCampusButton);
        controlRow.add(termChoose);
        controlRow.add(addDepartmentButton);
        controlRow.add(searchBox);
        controlRow.add(searchButton);
        controlRow.add(addButton);

        addButton.addActionListener(e -> {// String name, int number, int units, Department department
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
            nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame) frame, options, false);
            lowerOptions.setGridSize(2, 1);
            lowerOptions.setPadding(5);
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
            nFrame.ListLayout panel = new nFrame.ListLayout((nFrame) frame, enrollPanel, new Dimension(100, 100), 10,
                    10, false);
            panel.setPadding(5, 7);
            nPanelModal modal = new nPanelModal((nFrame) frame, panel, ww, hh);
            enrollButton.addActionListener(ee -> {
                // #region ADDS COURSE HERE
                if (campusChoose.getSelected() == null || departmentChoose.getSelected() == null) {
                    return;
                }
                Message<AddCourseResponse> addCourseResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_COURSE,
                        MessageStatus.REQUEST, new AddCourseRequest(
                                courseTB.getText(), Integer.valueOf(courseNumberTB.getText()),
                                Integer.valueOf(courseUnitsTB.getText()), campusChoose.getSelected().getText(),
                                departmentChoose.getSelected().getText(), new LinkedList<Course>()));
                searchButton.simulateClick();

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
                int rowHeight = 120;

                heading.setBounds(padding, padding, cw - padding * 2, headingHeight);

                int rowY = padding + headingHeight + padding;
                controlRow.setBounds(padding, rowY, cw - padding * 2, rowHeight);

                int listY = rowY + rowHeight + padding;
                int listH = ch - listY - padding;
                if (listH < 40)
                    listH = 40;

                manageCourseList.setBounds(padding, listY, cw - padding * 2, listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(controlRow);
        content.add(manageCourseList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }

    // #endregion
    // #region createReportsView
    public static nFrame.ListLayout createReportsView(nFrame frame, int x, int y, int w, int h) {
        nPanelPlainText heading = new nPanelPlainText("Generate Reports");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("Just a graph to show off the graphs while WIP");
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
    // #endregion

    private static void searchBrowseManageScrollableList(nFrame frame, nScrollableList list, String query,
            Course[] courses, UserRole userRole) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        list.clearItems();

        // #region just some temp logic for search query
        LinkedList<Course> ll = new LinkedList<Course>();
        for (Course c : courses) {
            if (!q.isEmpty()) {
                String haystack = (c.getName()).toLowerCase();
                if (!haystack.contains(q))
                    continue;
            }
            ll.Push(c);
        }
        // #endregion

        ll.forEach(c -> {
            list.addItem(new Panels.CourseItemPanel(c, userRole));
        });
        frame.resizeChildren();
        // printTree((Container) list, "root");
        // for debuging
    }

    private static void searchDropScrollableList(nFrame frame, nScrollableList list, Course[] courses,
            UserRole userRole) {
        String q = "".trim().toLowerCase();
        list.clearItems();

        // #region just some temp logic for search query
        LinkedList<Course> ll = new LinkedList<Course>();
        for (Course c : courses) {
            if (!q.isEmpty()) {
                String haystack = (c.getName()).toLowerCase();
                if (!haystack.contains(q))
                    continue;
            }
            ll.Push(c);
        }
        // #endregion

        ll.forEach(c -> {
            list.addItem(new Panels.DropItemPanel(c, userRole));
        });
        frame.resizeChildren();
        // printTree((Container) list, "root");
        // for debuging
    }

    private static void printTree(Container c, String currText) {
        Component[] children = c.getComponents();
        for (Component child : children) {
            Log.Msg(currText + "." + child.getName());
            if (child instanceof Container) {
                printTree((Container) child, currText + "." + child.getName());
                child.repaint();
            }
        }
    }
}
