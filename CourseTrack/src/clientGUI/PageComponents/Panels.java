package clientGUI.PageComponents;

import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;
import global.LinkedList;
import global.Log;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.data.Campus;
import global.data.Course;
import global.data.MeetTime;
import global.data.Section;
import global.data.StudentScheduleItem;
import global.data.Term;
import global.requests.AdminRequests.AdminGetCampusesRequest;
import global.requests.AdminRequests.AdminGetCoursesRequest;
import global.requests.AdminRequests.AdminGetSectionsRequest;
import global.requests.AdminRequests.AdminRemoveCourseRequest;
import global.requests.AdminRequests.AdminRemoveSectionRequest;
import global.responses.AdminResponses.AdminGetCampusesResponse;
import global.responses.AdminResponses.AdminGetCoursesResponse;
import global.responses.AdminResponses.AdminGetSectionsResponse;
import global.responses.AdminResponses.AdminRemoveCourseResponse;
import global.responses.AdminResponses.AdminRemoveSectionResponse;
import global.requests.*;
import global.responses.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JFrame;

import client.services.IAppGUIService;

public class Panels {

    public static class StudentCourseItemPanel extends nPanel {
        private Section course;
        private nButton[] buttonList;
        private Term currentTerm;
        StudentCourseItemPanel(Section _course, UserRole role, Term currentTerm, IAppGUIService guiService) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton enrollButton = new nButton("Enroll");
            enrollButton.setBackgroundColor(UITheme.SUCCESS);
            add(enrollButton);
            DoEnrollButton(enrollButton, guiService);
            buttonList = new nButton[1];
            buttonList[0] = enrollButton;

            doLayout();
        }

        private void DoEnrollButton(nButton EnrollButton, IAppGUIService guiService){
            EnrollButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 280;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                

                nButton enrollButton = new nButton("Enroll");
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
                Component[] enrollPanel = {
                    new nPanelPlainText(course.getDepartment().getCampus().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText(course.getDepartment().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText(course.getNumber()+" | "+course.getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Units: "+course.getCourse().getUnits(), UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<EnrollSectionResponse> enrollResponse = guiService.sendAndWait(MessageType.STUDENT_ENROLL, MessageStatus.REQUEST, new EnrollSectionRequest(course.getId(), currentTerm));
                    Log.Msg("Enroll Status: " + enrollResponse.getStatus().toString());
                    if(enrollResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        

        

        
        
        private JFrame getFrameWindow() {
            java.awt.Container c = getParent();
            while (c != null && !(c instanceof JFrame)) {
                c = c.getParent();
            }
            return (c instanceof JFrame) ? (JFrame) c : null;
        }

        @Override
        public void doLayout() {
            int padding = 10;
            int w = getWidth();
            int h = getHeight();

            int btnWidth = 80;
            int btnHeight = 26;
            int x = w - padding - btnWidth;
            int y = (h - btnHeight) / 2;
            for(int i = 0; i < buttonList.length; i++) {
                nButton button = buttonList[i];
                button.setBounds(x - i * ( btnWidth + padding ), y, btnWidth, btnHeight);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(UITheme.BG_APP);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2d.setColor(UITheme.BG_ELEVATED_ELEVATED);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            int x = 12;
            int y = 10;
            // Title line
            g2d.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
            g2d.setColor(UITheme.TEXT_PRIMARY);
            g2d.drawString(course.getNumber() + " - " + course.getName(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Instructor + time
            g2d.setFont(UITheme.FONT_BODY);
            g2d.setColor(UITheme.TEXT_MUTED);

            

            y += g2d.getFontMetrics().getAscent();
            
            String firstRow = "";
            String secondRow =  "Meet Times: ";
            String thirdRow = "Professor: " + course.getInstructor();
            for(MeetTime m : course.getMeetTimes()) {
                secondRow += m.getDay() + " " + m.getStartTime().toString() + "-" + m.getEndTime().toString();
                if(m.getDay().toString().equals(course.getMeetTimes()[course.getMeetTimes().length-1].getDay().toString())) {
                    continue;
                }
                secondRow+="/";
            }
            
            firstRow += "Capacity: " + course.getStudents().Length() + "/" + course.getCapacity() + " | Waitlisted: " + course.getWaitlist().Length();
            
            g2d.drawString(firstRow,x, y);
            
            g2d.drawString(secondRow,x, y + g2d.getFontMetrics().getAscent()+ 2 );
            g2d.drawString(thirdRow,x, y + g2d.getFontMetrics().getAscent()*2+ 4 );
            g2d.setRenderingHints(oldHints);
        }
    }
    

    public static class CourseItemPanel extends nPanel {
        private Course course;
        private nButton[] buttonList;
        private Term currentTerm;
        CourseItemPanel(Course _course, UserRole role, Term currentTerm, IAppGUIService guiService) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));
            
            switch (role) {
                case admin -> {
                    nButton editCourseButton = new nButton("Edit");
                    nButton deleteButton = new nButton("Delete");
                    editCourseButton.setBackgroundColor(UITheme.SUCCESS);
                    deleteButton.setBackgroundColor(UITheme.FAIL);
                    add(editCourseButton);
                    DoEditButton(editCourseButton, guiService);
                    add(deleteButton);
                    DoRemoveButton(deleteButton, guiService);
                    buttonList = new nButton[2];
                     buttonList[1] = editCourseButton; buttonList[0] = deleteButton;
                }
            
                default -> {
                    nButton enrollButton = new nButton("Enroll");
                    enrollButton.setBackgroundColor(UITheme.SUCCESS);
                    add(enrollButton);
                    DoEnrollButton(enrollButton, guiService);
                    buttonList = new nButton[1];
                    buttonList[0] = enrollButton;
                }
            }
            doLayout();
        }

        private void DoEnrollButton(nButton EnrollButton, IAppGUIService guiService){
            EnrollButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 280;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                

                nButton enrollButton = new nButton("Enroll");
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
                Component[] enrollPanel = {
                    new nPanelPlainText(course.getDepartment().getCampus().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText(course.getDepartment().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText(course.getNumber()+" | "+course.getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Units: "+course.getUnits(), UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<EnrollSectionResponse> enrollResponse = guiService.sendAndWait(MessageType.STUDENT_ENROLL, MessageStatus.REQUEST, new EnrollSectionRequest(course.getId(), currentTerm));
                    Log.Msg("Enroll Status: " + enrollResponse.getStatus().toString());
                    if(enrollResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        

        private void DoEditButton(nButton EditButton, IAppGUIService guiService){
            EditButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 520;
                int h = 500;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                
                nButton enrollButton = new nButton("Create New Section");
                enrollButton.setBackgroundColor(UITheme.SUCCESS);

                
                


                nScrollableList manageCourseList = new nScrollableList();
                manageCourseList.setInnerPadding(8);
                manageCourseList.setItemSpacing(8);
                Component[] topPanel = {
                    new nPanelPlainText("Select a Section to Edit", UITheme.TEXT_PRIMARY),
                    enrollButton,
                };
                nFrame.ListLayout TopPanel = new nFrame.ListLayout((nFrame)frame, topPanel, new Dimension(100, 100), 10, 10, false);
                Component[] enrollPanel = {
                    TopPanel,
                    manageCourseList
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);

                enrollButton.addActionListener(ee->{
                    Message<AddSectionResponse> addCourseResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_SECTION,
                        MessageStatus.REQUEST, new AddSectionRequest(
                            course.getId(),
                            course.getCampus().getName(),
                            course.getDepartment().getName(),
                            currentTerm,
                            "",
                            0,
                            MeetTime.random()
                        ));
                    Section s = addCourseResponse.get().section();
                    SectionItemPanel p = new Panels.SectionItemPanel(s, currentTerm, guiService, modal, (nFrame)frame);
                    manageCourseList.addItem(p);
                    p.editSectionButton.simulateClick();
                });
                Message<AdminGetSectionsResponse> ar = guiService.sendAndWait(
                    MessageType.ADMIN_GET_SECTIONS,
                    MessageStatus.REQUEST,
                    new AdminGetSectionsRequest());
                LinkedList<Section> courses = ar.get().sections();
                if(courses == null) {courses = new LinkedList<Section>();}
                    
                
                for(Section s : courses) {
                    if(!s.getName().equals(course.getName())){continue;}
                    manageCourseList.addItem(new Panels.SectionItemPanel(s, currentTerm, guiService, modal, (nFrame)frame));
                }

                
                modal.revalidate();
                modal.repaint();
            });
        
        }
        
        private void DoRemoveButton(nButton RemoveButton, IAppGUIService guiService){
            RemoveButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 200;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                

                nButton enrollButton = new nButton("Yes");
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
                Component[] enrollPanel = {
                    new nPanelPlainText("Are you sure you want to delete " + course.getName() + " and all of its sections?", UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<AdminRemoveCourseResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_COURSE,
                            MessageStatus.REQUEST, new AdminRemoveCourseRequest(course.getCampus().getName(), course.getDepartment().getName(), course));
                    if(removeResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        


        
        
        private JFrame getFrameWindow() {
            java.awt.Container c = getParent();
            while (c != null && !(c instanceof JFrame)) {
                c = c.getParent();
            }
            return (c instanceof JFrame) ? (JFrame) c : null;
        }

        @Override
        public void doLayout() {
            int padding = 10;
            int w = getWidth();
            int h = getHeight();

            int btnWidth = 80;
            int btnHeight = 26;
            int x = w - padding - btnWidth;
            int y = (h - btnHeight) / 2;
            for(int i = 0; i < buttonList.length; i++) {
                nButton button = buttonList[i];
                button.setBounds(x - i * ( btnWidth + padding ), y, btnWidth, btnHeight);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(UITheme.BG_APP);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2d.setColor(UITheme.BG_ELEVATED_ELEVATED);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            int x = 12;
            int y = 10;
            // Title line
            g2d.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
            g2d.setColor(UITheme.TEXT_PRIMARY);
            g2d.drawString(course.getNumber() + " - " + course.getName(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Instructor + time
            g2d.setFont(UITheme.FONT_BODY);
            g2d.setColor(UITheme.TEXT_MUTED);

            

            y += g2d.getFontMetrics().getAscent();
            //int numsections = course.getSections(currentTerm) == null ? 0 : course.getSections(currentTerm).Length();
            //g2d.drawString("Sections: " + numsections,x, y + g2d.getFontMetrics().getAscent()+ 2 );
            g2d.setRenderingHints(oldHints);
        }
    }
    
    
    public static class SectionItemPanel extends nPanel {
        private Section course;
        private nButton[] buttonList;
        private Term currentTerm;
        public nButton editSectionButton;
        public nButton deleteButton;
        SectionItemPanel(Section _course, Term currentTerm, IAppGUIService guiService, nPanelModal modal, nFrame frame) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            editSectionButton= new nButton("Edit");
            deleteButton = new nButton("Delete");
            editSectionButton.setBackgroundColor(UITheme.SUCCESS);
            deleteButton.setBackgroundColor(UITheme.FAIL);
            add(editSectionButton, modal);
            DoEditSectionButton(editSectionButton, guiService, modal, frame);
            add(deleteButton, modal);
            DoRemoveButton(deleteButton, guiService, modal, frame);
            buttonList = new nButton[2];
            buttonList[1] = editSectionButton; buttonList[0] = deleteButton;
                
            doLayout();
        }

        
        private void DoEditSectionButton(nButton EditButton, IAppGUIService guiService, nPanelModal parentmodal, nFrame frame){
            EditButton.addActionListener(e -> {
                parentmodal.close();
                int w = 420;
                int h = 400;

                

                nButton enrollButton = new nButton("Save");
                enrollButton.setBackgroundColor(UITheme.SUCCESS);
                nButton cancelButton = new nButton("Cancel");
                cancelButton.setBackgroundColor(UITheme.FAIL);
                Component[] options = {
                    enrollButton,
                    cancelButton,
                };
                nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options, false);
                lowerOptions.setGridSize(2, 1);
                lowerOptions.setPadding(5);
                nPanelTextBox instructorTB = new nPanelTextBox(course.getInstructor(), UITheme.TEXT_PRIMARY);
                nPanelTextBox capacityTB = new nPanelTextBox(""+course.getCapacity(), UITheme.TEXT_PRIMARY);
                Component[] enrollPanel = {
                    new nPanelPlainText("Edit Section", UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Instructor", UITheme.TEXT_PRIMARY),
                    instructorTB,
                    new nPanelPlainText("Capacity", UITheme.TEXT_PRIMARY),
                    capacityTB,
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<AdminRemoveSectionResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_SECTION,
                            MessageStatus.REQUEST, new AdminRemoveSectionRequest(course.getCourse(), currentTerm, course));
                    if(removeResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    
                    Message<AddSectionResponse> addResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_SECTION,
                            MessageStatus.REQUEST, new AddSectionRequest(course.getCourse().getId(), course.getCourse().getCampus().getName(), course.getDepartment().getName(), course.getTerm(), instructorTB.getText(), Integer.valueOf(capacityTB.getText()), course.getMeetTimes()));
                    if(addResponse.getStatus() != MessageStatus.SUCCESS) {return;}

                    course = addResponse.get().section();

                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        private void DoRemoveButton(nButton RemoveButton, IAppGUIService guiService, nPanelModal parentmodal, nFrame frame){
            RemoveButton.addActionListener(e -> {
                parentmodal.close();
                
                int w = 420;
                int h = 200;

                

                nButton enrollButton = new nButton("Yes");
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
                Component[] enrollPanel = {
                    new nPanelPlainText("Are you sure you want to delete " + course.getName() + " ?", UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<AdminRemoveSectionResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_SECTION,
                            MessageStatus.REQUEST, new AdminRemoveSectionRequest(course.getCourse(), currentTerm, course));
                    if(removeResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        

        @Override
        public void doLayout() {
            int padding = 10;
            int w = getWidth();
            int h = getHeight();

            int btnWidth = 80;
            int btnHeight = 26;
            int x = w - padding - btnWidth;
            int y = padding;
            for(int i = 0; i < buttonList.length; i++) {
                nButton button = buttonList[i];
                button.setBounds(x - i * ( btnWidth + padding ), y, btnWidth, btnHeight);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(UITheme.BG_APP);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2d.setColor(UITheme.BG_ELEVATED_ELEVATED);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            int x = 12;
            int y = 10;
            // Title line
            g2d.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
            g2d.setColor(UITheme.TEXT_PRIMARY);
            g2d.drawString(course.getCourse().getNumber() + " - " + course.getName(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Instructor + time
            g2d.setFont(UITheme.FONT_BODY);
            g2d.setColor(UITheme.TEXT_MUTED);
            String firstRow = "";
            String secondRow =  "Meet Times: ";
            String thirdRow = "Professor: " + course.getInstructor();
            for(MeetTime m : course.getMeetTimes()) {
                secondRow += m.getDay() + " " + m.getStartTime().toString() + "-" + m.getEndTime().toString();
                if(m.getDay().toString().equals(course.getMeetTimes()[course.getMeetTimes().length-1].getDay().toString())) {
                    continue;
                }
                secondRow+="/";
            }
            
            firstRow += "Capacity: " + course.getStudents().Length() + "/" + course.getCapacity() + " | Waitlisted: " + course.getWaitlist().Length();
            y += g2d.getFontMetrics().getAscent();
            g2d.drawString(firstRow,x, y);
            
            g2d.drawString(secondRow,x, y + g2d.getFontMetrics().getAscent()+ 2 );
            g2d.drawString(thirdRow,x, y + g2d.getFontMetrics().getAscent()*2+ 4 );
            g2d.setRenderingHints(oldHints);
        }
    }
    
    
    public static class DropItemPanel extends nPanel {
        private Section course;
        private nButton[] buttonList;
        private Term currentTerm;
        DropItemPanel(Section _course, Term currentTerm, IAppGUIService guiService, nButton b) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton DropButton = new nButton("Drop");
            DropButton.setBackgroundColor(UITheme.SUCCESS);
            add(DropButton);
            DoDropButton(DropButton, guiService, b);
            buttonList = new nButton[1];
            buttonList[0] = DropButton;
            doLayout();
        }
        private void DoDropButton(nButton DropButton, IAppGUIService guiService, nButton b){
            DropButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 280;

                nButton enrollButton = new nButton("Drop");
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
                Component[] enrollPanel = {
                    new nPanelPlainText("Are you sure you want to drop this course?", UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<DropSectionResponse> dropSectionRequest = guiService.sendAndWait(
                        MessageType.STUDENT_DROP, MessageStatus.REQUEST,
                        new DropSectionRequest(course.getId(), course.getTerm()));
                    Log.Msg("Drop Status: " + dropSectionRequest.getStatus().toString());
                    if(dropSectionRequest.getStatus() != MessageStatus.SUCCESS) {return;}
                    b.simulateClick();
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        private JFrame getFrameWindow() {
            java.awt.Container c = getParent();
            while (c != null && !(c instanceof JFrame)) {
                c = c.getParent();
            }
            return (c instanceof JFrame) ? (JFrame) c : null;
        }

        @Override
        public void doLayout() {
            int padding = 10;
            int w = getWidth();
            int h = getHeight();

            int btnWidth = 80;
            int btnHeight = 26;
            int x = w - padding - btnWidth;
            int y = (h - btnHeight) / 2;
            for(int i = 0; i < buttonList.length; i++) {
                nButton button = buttonList[i];
                button.setBounds(x - i * ( btnWidth + padding ), y, btnWidth, btnHeight);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(UITheme.BG_APP);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2d.setColor(UITheme.BG_ELEVATED_ELEVATED);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            int x = 12;
            int y = 10;
            // Title line
            g2d.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
            g2d.setColor(UITheme.TEXT_PRIMARY);
            g2d.drawString(course.getNumber() + " - " + course.getName(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            g2d.setFont(UITheme.FONT_BODY);
            g2d.setColor(UITheme.TEXT_MUTED);
            String firstRow = "";
            String secondRow =  "Meet Times: ";
            String thirdRow = "Professor: " + course.getInstructor();
            for(MeetTime m : course.getMeetTimes()) {
                secondRow += m.getDay() + " " + m.getStartTime().toString() + "-" + m.getEndTime().toString();
                if(m.getDay().toString().equals(course.getMeetTimes()[course.getMeetTimes().length-1].getDay().toString())) {
                    continue;
                }
                secondRow+="/";
            }
            
            firstRow += "Capacity: " + course.getStudents().Length() + "/" + course.getCapacity() + " | Waitlisted: " + course.getWaitlist().Length();
            y += g2d.getFontMetrics().getAscent();
            g2d.drawString(firstRow,x, y);
            
            g2d.drawString(secondRow,x, y + g2d.getFontMetrics().getAscent()+ 2 );
            g2d.drawString(thirdRow,x, y + g2d.getFontMetrics().getAscent()*2+ 4 );
            g2d.setRenderingHints(oldHints);
        }
    }
    public static class WaitlistItemPanel extends nPanel {
        private StudentScheduleItem course;
        private nButton[] buttonList;
        WaitlistItemPanel(StudentScheduleItem course, IAppGUIService guiService, nButton b) {
            setName("CourseItemPanel");
            this.course = course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton DropButton = new nButton("Unqueue");
            DropButton.setBackgroundColor(UITheme.SUCCESS);
            add(DropButton);
            DoDropButton(DropButton, guiService, b);
            buttonList = new nButton[1];
            buttonList[0] = DropButton;
            doLayout();
        }
        private void DoDropButton(nButton DropButton, IAppGUIService guiService, nButton b){
            DropButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 280;

                nButton enrollButton = new nButton("Leave Queue");
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
                Component[] enrollPanel = {
                    new nPanelPlainText("Are you sure you want to leave the queue?", UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<DropSectionResponse> dropSectionRequest = guiService.sendAndWait(
                        MessageType.STUDENT_DROP, MessageStatus.REQUEST,
                        new DropSectionRequest(course.getSection().getId(), course.getSection().getTerm()));
                    if(dropSectionRequest.getStatus() != MessageStatus.SUCCESS) {return;}
                    b.simulateClick();
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        private JFrame getFrameWindow() {
            java.awt.Container c = getParent();
            while (c != null && !(c instanceof JFrame)) {
                c = c.getParent();
            }
            return (c instanceof JFrame) ? (JFrame) c : null;
        }

        @Override
        public void doLayout() {
            int padding = 10;
            int w = getWidth();
            int h = getHeight();

            int btnWidth = 80;
            int btnHeight = 26;
            int x = w - padding - btnWidth;
            int y = (h - btnHeight) / 2;
            for(int i = 0; i < buttonList.length; i++) {
                nButton button = buttonList[i];
                button.setBounds(x - i * ( btnWidth + padding ), y, btnWidth, btnHeight);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(UITheme.BG_APP);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2d.setColor(UITheme.BG_ELEVATED_ELEVATED);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            int x = 12;
            int y = 10;
            // Title line
            g2d.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
            g2d.setColor(UITheme.TEXT_PRIMARY);
            g2d.drawString(course.getSection().getNumber() + " - " + course.getSection().getName() + " | Waitlist Position: " + course.getWaitlistPosition(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Instructor + time
            g2d.setFont(UITheme.FONT_BODY);
            String firstRow = "";
            String secondRow =  "Meet Times: ";
            String thirdRow = "Professor: " + course.getSection().getInstructor();
            for(MeetTime m : course.getSection().getMeetTimes()) {
                secondRow += m.getDay() + " " + m.getStartTime().toString() + "-" + m.getEndTime().toString();
                if(m.getDay().toString().equals(course.getSection().getMeetTimes()[course.getSection().getMeetTimes().length-1].getDay().toString())) {
                    continue;
                }
                secondRow+="/";
            }
            
            firstRow += "Capacity: " + course.getSection().getStudents().Length() + "/" + course.getSection().getCapacity() + " | Waitlist Position: " + course.getWaitlistPosition();
            y += g2d.getFontMetrics().getAscent();
            g2d.drawString(firstRow,x, y);
            
            g2d.drawString(secondRow,x, y + g2d.getFontMetrics().getAscent()+ 2 );
            g2d.drawString(thirdRow,x, y + g2d.getFontMetrics().getAscent()*2+ 4 );
            g2d.setRenderingHints(oldHints);
        }
    }

}
