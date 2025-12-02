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
import global.data.StudentScheduleItem;
import global.data.Term;
import global.requests.*;
import global.requests.AdminRequests.*;
import global.responses.*;
import global.responses.AdminResponses.*;
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
    public static nFrame.ListLayout createBrowseView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService,UserRole userRole, nButton goToButton) {
        
        

        Message<GetCampusesResponse> response = guiService.sendAndWait(MessageType.GET_CAMPUSES, MessageStatus.REQUEST, new GetCampusesRequest());
        

        
        LinkedList<Campus> campuses = response.get().campuses();
        if(campuses == null) {campuses = new LinkedList<Campus>();}



        
        

        nPanelDropDown campusChoose = new nPanelDropDown();
        nPanelDropDown departmentChoose = new nPanelDropDown();
        nPanelDropDown termChoose = new nPanelDropDown();


        nPanelPlainText heading = new nPanelPlainText("Browse Courses");
        heading.textColor = UITheme.TEXT_PRIMARY;

        // search controls
        nPanelTextBox searchBox = new nPanelTextBox();
        searchBox.textColor = UITheme.TEXT_PRIMARY;
        searchBox.backgroundColor = UITheme.BG_APP;
        searchBox.drawBorder = true;
        
        nButton searchButton = new nButton("Search");
        searchButton.setBackgroundColor(UITheme.INFO);

        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);

        searchButton.addActionListener(e -> {
            courseList.clearItems();
            String query = searchBox.getText();


            Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
            LinkedList<Term> terms = getTermsResponse.get().terms();
            if(terms == null) {return;}
            Term cTerm = terms.Get(0);
            for(Term t : terms) {
                cTerm = t;
                if(!t.getDisplayName().equals(termChoose.getSelected().getText())) {continue;}
                break;                      
            }
            Message<BrowseSectionResponse> getCoursesResponse = guiService.sendAndWait(
                    MessageType.STUDENT_BROWSE_SECTION, MessageStatus.REQUEST, new BrowseSectionRequest(query, campusChoose.getSelected().getText(), departmentChoose.getSelected().getText(), cTerm, 100));
            LinkedList<Section> sections = getCoursesResponse.get().sections();

            
            if(sections == null) {sections = new LinkedList<Section>();}
            for (Section c : sections) {
                courseList.addItem(new Panels.StudentCourseItemPanel(c, userRole, c.getTerm(), guiService));
            }

            courseList.revalidate();
            courseList.repaint();
        });
        

        for (Campus campus : campuses) {
            nButton b = new nButton();
            b.setText(campus.getName());
            campusChoose.addOption(b);
            b.addActionListener(e -> {
                courseList.clearItems();
                departmentChoose.clearOptions();
                termChoose.clearOptions();

                

                LinkedList<Department> departments = campus.getDepartments();// = getDepartmentsResponse.get().departments();
                //if(departments)
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

                courseList.revalidate();
                departmentChoose.revalidate();
                campusChoose.revalidate();
                courseList.repaint();
                departmentChoose.repaint();
                campusChoose.repaint();
            });

            nButton selected = campusChoose.getSelected();
            if (selected != null && campus.getName().equals(selected.getText())) {
                b.simulateClick();
                goToButton.addActionListener(e->{b.simulateClick();});
            }

        }

        nPanel searchRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = 80;
                int w = getWidth();
                int buttonWidth = 80;
                int controlHeight = 28;

                int boxWidth = w - buttonWidth - padding * 2;
                if (boxWidth < 80)
                    boxWidth = 80;

                int yMid = h/2;

                campusChoose.setBounds(padding, 0, (int) (w * 0.33) - padding, controlHeight);
                
                departmentChoose.setBounds(padding + (int) (w*0.33), 0, (int) (w * 0.33) - padding, controlHeight);
                
                termChoose.setBounds(padding + (int) (w*0.66), 0, (int) (w * 0.33) - padding, controlHeight);

                int bx = padding * 2 + boxWidth;
                
                

                searchBox.setBounds(padding, yMid, boxWidth, controlHeight);
                searchButton.setBounds(bx, yMid, buttonWidth, controlHeight);

            }
        };
        searchRow.setLayout(null);
        searchRow.setOpaque(false);
        searchRow.add(searchBox);
        searchRow.add(searchButton);
        searchRow.add(departmentChoose);
        searchRow.add(campusChoose);
        searchRow.add(termChoose);


        
        
        
        // content panel inside the card: heading at top, searchRow, then list filling
        // rest
        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int w = getWidth();
                int h = getHeight();

                int headingHeight = 32;
                int searchRowHeight = 80;

                heading.setBounds(padding, padding, w - padding * 2, headingHeight);

                int searchY = padding + headingHeight + padding;
                searchRow.setBounds(padding, searchY, w - padding * 2, h-searchY);

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
    public static nFrame.ListLayout createDropView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService, nButton goToButton) {
        // heading
        nPanelPlainText heading = new nPanelPlainText("Schedule");
        heading.textColor = UITheme.TEXT_PRIMARY;
        nPanelDropDown termChoose = new nPanelDropDown();
        // scrollable course list
        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);
        
        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
        LinkedList<Term> terms = getTermsResponse.get().terms();
        for(Term t : terms) {
            nButton b = new nButton();
            b.setText(t.getDisplayName());
            termChoose.addOption(b);
            b.addActionListener(e -> {
                courseList.clearItems();
                Message<GetScheduleResponse> getCoursesResponse = guiService.sendAndWait(
                        MessageType.STUDENT_GET_SCHEDULE, MessageStatus.REQUEST, new GetScheduleRequest(t));
                LinkedList<StudentScheduleItem> sections = getCoursesResponse.get().schedule();
                if(sections == null) {sections = new LinkedList<StudentScheduleItem>();}
                for (StudentScheduleItem c : sections) {
                    if(c.getWaitlistPosition() == 0) {
                        courseList.addItem(new Panels.DropItemPanel(c.getSection(), c.getSection().getTerm(), guiService, b));
                    }
                }
                courseList.revalidate();
                courseList.repaint();
            });
            if (termChoose.getSelected().getText().equals(t.getDisplayName())) {
                b.simulateClick();
                goToButton.addActionListener(e->{b.simulateClick();});
            }
            
        }
        

        nPanel searchRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = 40;
                int w = getWidth();
                int buttonWidth = 80;
                int controlHeight = 28;

                

                termChoose.setBounds(padding, 0, w-padding*2, controlHeight);


            }
        };
        searchRow.setLayout(null);
        searchRow.setOpaque(false);
        searchRow.add(termChoose);
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
                searchRow.setBounds(padding, searchY, w - padding * 2, h-searchY);

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
    // #region createWaitlistView
    public static nFrame.ListLayout createWaitlistView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService, nButton goToButton) {
        nPanelPlainText heading = new nPanelPlainText("Waitlist");
        heading.textColor = UITheme.TEXT_PRIMARY;
        nPanelDropDown termChoose = new nPanelDropDown();
        // scrollable course list
        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);
        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
        LinkedList<Term> terms = getTermsResponse.get().terms();
        for(Term t : terms) {
            nButton b = new nButton();
            b.setText(t.getDisplayName());
            termChoose.addOption(b);
            b.addActionListener(e -> {
                courseList.clearItems();
                Message<GetScheduleResponse> getCoursesResponse = guiService.sendAndWait(
                        MessageType.STUDENT_GET_SCHEDULE, MessageStatus.REQUEST, new GetScheduleRequest(t));
                LinkedList<StudentScheduleItem> sections = getCoursesResponse.get().schedule();
                if(sections == null) {sections = new LinkedList<StudentScheduleItem>();}
                for (StudentScheduleItem c : sections) {
                    if(c.getWaitlistPosition() > 0) {
                        courseList.addItem(new Panels.WaitlistItemPanel(c, guiService, b));
                    }
                }
                courseList.revalidate();
                courseList.repaint();
            });
            if (termChoose.getSelected().getText().equals(t.getDisplayName())) {
                b.simulateClick();
                goToButton.addActionListener(e->{b.simulateClick();});
            }
            
        }
        

        nPanel searchRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = 40;
                int w = getWidth();
                int buttonWidth = 80;
                int controlHeight = 28;

                

                termChoose.setBounds(padding, 0, w-padding*2, controlHeight);


            }
        };
        searchRow.setLayout(null);
        searchRow.setOpaque(false);
        searchRow.add(termChoose);
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
                searchRow.setBounds(padding, searchY, w - padding * 2, h-searchY);

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
        content.add(courseList);
        content.add(searchRow);
        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }

    // #endregion

    
    // #region createScheduleView
    @Deprecated
    public static nFrame.ListLayout createScheduleView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService) {
        nPanelPlainText heading = new nPanelPlainText("Schedule");
        heading.textColor = UITheme.TEXT_PRIMARY;
        nPanelDropDown termChoose = new nPanelDropDown();
        // scrollable course list
        nScrollableList courseList = new nScrollableList();
        courseList.setInnerPadding(8);
        courseList.setItemSpacing(8);
        
        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
        LinkedList<Term> terms = getTermsResponse.get().terms();
        if(terms == null) {terms = new LinkedList<Term>();}
        for(Term t : terms) {
            nButton b = new nButton();
            b.setText(t.getDisplayName());
            termChoose.addOption(b);
            b.addActionListener(e -> {
                courseList.clearItems();
                Message<GetScheduleResponse> getCoursesResponse = guiService.sendAndWait(
                        MessageType.STUDENT_GET_SCHEDULE, MessageStatus.REQUEST, new GetScheduleRequest(t));
                LinkedList<StudentScheduleItem> sections = getCoursesResponse.get().schedule();
                if(sections == null) {sections = new LinkedList<StudentScheduleItem>();}
                for (StudentScheduleItem c : sections) {
                    if(c.getWaitlistPosition() == 0) {
                        courseList.addItem(new Panels.DropItemPanel(c.getSection(), c.getSection().getTerm(), guiService, b));
                    }
                }
                courseList.revalidate();
                courseList.repaint();
            });
            if (termChoose.getSelected().getText().equals(t.getDisplayName())) {
                b.simulateClick();
                
            }
            
        }
        

        nPanel searchRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int h = getHeight();
                int w = getWidth();
                int buttonWidth = 80;
                int controlHeight = 28;

                

                termChoose.setBounds(padding, 0, w-padding*2, controlHeight);


            }
        };
        searchRow.setLayout(null);
        searchRow.setOpaque(false);
        searchRow.add(termChoose);
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
                setComponentZOrder(courseList, getComponentZOrder(searchRow)+1);
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


            Message<AdminGetCoursesResponse> getSectionsResponse = guiService.sendAndWait(
                    MessageType.ADMIN_GET_COURSES, MessageStatus.REQUEST, new AdminGetCoursesRequest());
            LinkedList<Course> sections = getSectionsResponse.get().courses();


            Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                    MessageStatus.REQUEST, new GetTermsRequest());
            LinkedList<Term> terms = getTermsResponse.get().terms();

            if(sections == null) {sections = new LinkedList<Course>();}
            for (Course c : sections) {
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
                    if(terms == null) {terms = new LinkedList<Term>();}
                    for(Term t : terms) {
                        if(!t.getDisplayName().equals(termChoose.getSelected().getText())) {continue;}
                        
                        manageCourseList.addItem(new Panels.CourseItemPanel(c, userRole, t, guiService));
                        
                    }
                    
                }
            }

            manageCourseList.revalidate();
            manageCourseList.repaint();
        });
        if(campuses == null) {campuses = new LinkedList<Campus>();}
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
                if(departments == null) {departments = new LinkedList<Department>();}
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
                        if(terms == null) {terms = new LinkedList<Term>();}
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
                            db.addActionListener(eeee -> {
                                termChoose.clearOptions();

                                Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS,
                                        MessageStatus.REQUEST, new GetTermsRequest());
                                LinkedList<Term> terms = getTermsResponse.get().terms();
                                if(terms == null) {terms = new LinkedList<Term>();}
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
                        if(terms == null) {terms= new LinkedList<Term>();}
                        for (Term t : terms) {
                            nButton tb = new nButton();
                            tb.setText(t.getDisplayName());
                            termChoose.addOption(tb);
                        }

                        searchButton.simulateClick();

                        departmentChoose.revalidate();
                        departmentChoose.repaint();
                    });
                    b.simulateClick();
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
                int h = 120;
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
                controlRow.setBounds(padding, rowY, cw - padding * 2, h-rowY);

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
    public static nFrame.ListLayout createReportsView(nFrame frame, int x, int y, int w, int h, IAppGUIService guiService, UserRole userRole) {
        Message<GetTermsResponse> getTermsResponse = guiService.sendAndWait(MessageType.GET_TERMS, MessageStatus.REQUEST,
                new GetTermsRequest());
        LinkedList<Term> terms = getTermsResponse.get().terms();
        final LinkedList<Term> termsFinal = terms; 

        if (terms == null) {
            terms = new LinkedList<Term>();
        }
        nPanelPlainText heading = new nPanelPlainText("Generate Reports");
        heading.textColor = UITheme.TEXT_PRIMARY;

        nPanelPlainText desc = new nPanelPlainText("View enrollment and waitlist stats for each section.");
        desc.textColor = UITheme.TEXT_MUTED;

        nPanelDropDown termChoose = new nPanelDropDown();
        termChoose.setDropDownHeight(140);

        nButton loadButton = new nButton("Load Report");
        loadButton.setBackgroundColor(UITheme.INFO);

        nScrollableList reportList = new nScrollableList();
        reportList.setInnerPadding(8);
        reportList.setItemSpacing(8);

        for (Term term : terms) {
            nButton termButton = new nButton(term.getDisplayName());
            termChoose.addOption(termButton);
        }

        Runnable loadReports = () -> {
            reportList.clearItems();
            nButton selected = termChoose.getSelected();
            if (selected == null) {
                reportList.revalidate();
                reportList.repaint();
                return;
            }
          
        Term selectedTerm = null;
            for (Term term : termsFinal) {
                if (term != null && term.getDisplayName().equals(selected.getText())) {
                    selectedTerm = term;
                    break;
                }
            }

        if (selectedTerm == null) {
                reportList.revalidate();
                reportList.repaint();
                return;
            }

            Message<DisplayReport> reportResponse = guiService.sendAndWait(MessageType.ADMIN_GET_REPORT,
                    MessageStatus.REQUEST, new ReportRequest(selectedTerm));
            LinkedList<String> reportEntries = reportResponse.get().reportEntries();
            if (reportEntries == null) {
                reportEntries = new LinkedList<String>();
            }

            for (String entry : reportEntries) {
                reportList.addItem(createReportEntryPanel(frame, entry));
            }

            reportList.revalidate();
            reportList.repaint();
        };

        loadButton.addActionListener(e -> {
            loadReports.run();
        });

        if (termChoose.getSelected() != null) {
            loadReports.run();
        }

        nPanel controlRow = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int w = getWidth();
                int h = getHeight();

                int buttonWidth = 120;
                int controlHeight = h - padding * 2;
                if (controlHeight < 28) {
                    controlHeight = 28;
                }

                termChoose.setBounds(padding, padding, w - buttonWidth - padding * 3, controlHeight);
                loadButton.setBounds(w - buttonWidth - padding, padding, buttonWidth, controlHeight);
            }
        };
        controlRow.setLayout(null);
        controlRow.setOpaque(false);
        controlRow.add(termChoose);
        controlRow.add(loadButton);

        nPanel content = new nPanel() {
            @Override
            public void doLayout() {
                int padding = 10;
                int cw = getWidth();
                int ch = getHeight();

                int headingHeight = 32;
                int descHeight = 24;
                int controlHeight = 48;

                heading.setBounds(padding, padding, cw - padding * 2, headingHeight);
                desc.setBounds(padding, padding + headingHeight, cw - padding * 2, descHeight);

                int controlY = padding + headingHeight + descHeight + padding;
                controlRow.setBounds(padding, controlY, cw - padding * 2, controlHeight);

                int listY = controlY + controlHeight + padding;
                int listH = ch - listY - padding;
                if (listH < 40) {
                    listH = 40;
                }

                reportList.setBounds(padding, listY, cw - padding * 2, listH);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        content.add(heading);
        content.add(desc);
        content.add(controlRow);
        content.add(reportList);

        Component[] comps = { content };

        nFrame.ListLayout layout = new nFrame.ListLayout(frame, comps, new Dimension(w, h), x, y);
        layout.backgroundColor = UITheme.BG_ELEVATED2;
        layout.setPadding(10, 10);
        layout.setStyle(nFrame.ListLayout.Style.NONE);
        return layout;
    }
    // #endregion

    private static nFrame.ListLayout createReportEntryPanel(nFrame frame, String entry) {
        if (entry == null) {
            entry = "";
        }

        String[] lines = entry.split("\\n");
        Component[] lineComponents = new Component[lines.length];
        for (int i = 0; i < lines.length; i++) {
            lineComponents[i] = new nPanelPlainText(lines[i], UITheme.TEXT_PRIMARY);
        }

        nFrame.ListLayout entryPanel = new nFrame.ListLayout(frame, lineComponents, new Dimension(100, 100), 0, 0, false);
        entryPanel.setPadding(4, 4);
        entryPanel.backgroundColor = UITheme.BG_ELEVATED;
        return entryPanel;
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
