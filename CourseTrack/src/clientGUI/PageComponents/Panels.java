package clientGUI.PageComponents;

import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;
import global.LinkedList;
import global.Message;
import global.MessageStatus;
import global.MessageType;
import global.data.Campus;
import global.data.Course;
import global.data.MeetTime;
import global.data.Section;
import global.data.StudentScheduleItem;
import global.data.Term;
import global.requests.AddCourseRequest;
import global.requests.AddSectionRequest;
import global.requests.AdminRequests.AdminGetCampusesRequest;
import global.requests.AdminRequests.AdminRemoveCourseRequest;
import global.requests.AdminRequests.AdminRemoveSectionRequest;
import global.requests.EnrollSectionRequest;
import global.responses.AddCourseResponse;
import global.responses.AddSectionResponse;
import global.responses.EnrollSectionResponse;
import global.responses.AdminResponses.AdminGetCampusesResponse;
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
    public static class CourseItemPanel extends nPanel {
        private Section course;
        private nButton[] buttonList;
        private Term currentTerm;
        CourseItemPanel(Section _course, UserRole role, Term currentTerm, IAppGUIService guiService) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));
            
            switch (role) {
                case admin -> {
                    nButton editCourseButton = new nButton("Edit Course");
                    nButton editSectionButton = new nButton("Edit");
                    nButton deleteButton = new nButton("Delete");
                    editCourseButton.setBackgroundColor(UITheme.SUCCESS);
                    editSectionButton.setBackgroundColor(UITheme.SUCCESS);
                    deleteButton.setBackgroundColor(UITheme.FAIL);
                    add(editCourseButton);
                    DoEditButton(editCourseButton, guiService);
                    add(editSectionButton);
                    DoEditSectionButton(editSectionButton, guiService);
                    add(deleteButton);
                    DoRemoveButton(deleteButton, guiService);
                    buttonList = new nButton[3];
                    buttonList[2] = editSectionButton; buttonList[1] = editCourseButton; buttonList[0] = deleteButton;
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
                    new nPanelPlainText("Units: "+course.getCourse().getUnits(), UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<EnrollSectionResponse> enrollResponse = guiService.sendAndWait(MessageType.STUDENT_ENROLL, MessageStatus.REQUEST, new EnrollSectionRequest(course.getId(), course.getTerm()));
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
                int w = 420;
                int h = 400;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                

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
                nPanelTextBox courseTB = new nPanelTextBox(course.getName(), UITheme.TEXT_PRIMARY);
                nPanelTextBox courseNumberTB = new nPanelTextBox(""+course.getNumber(), UITheme.TEXT_PRIMARY);
                nPanelTextBox courseUnitsTB = new nPanelTextBox(""+course.getCourse().getUnits(), UITheme.TEXT_PRIMARY);
                Component[] enrollPanel = {
                    new nPanelPlainText("Edit Course", UITheme.TEXT_PRIMARY),
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
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<AdminRemoveCourseResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_COURSE,
                            MessageStatus.REQUEST, new AdminRemoveCourseRequest(course.getCourse().getCampus().getName(), course.getCourse().getDepartment().getName(), course.getCourse()));
                    if(removeResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    
                    Message<AddCourseResponse> addResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_COURSE,
                            MessageStatus.REQUEST, new AddCourseRequest(courseTB.getText(), Integer.valueOf(courseNumberTB.getText()), Integer.valueOf(courseUnitsTB.getText()), course.getCourse().getCampus().getName(), course.getDepartment().getName(), course.getCourse().getRequirements()));
                    if(addResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                        
                    for(Section s : addResponse.get().course().getSections(currentTerm)) {
                        if(s.getId() == course.getId()) {
                            course = s;
                            break;
                        }
                    }
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        private void DoEditSectionButton(nButton EditButton, IAppGUIService guiService){
            EditButton.addActionListener(e -> {
                JFrame frame = getFrameWindow();
                int w = 420;
                int h = 400;
                int x = (frame.getWidth() - w) / 2;
                int y = (frame.getHeight() - h) / 2;

                

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
                    Message<AdminRemoveSectionResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_COURSE,
                            MessageStatus.REQUEST, new AdminRemoveSectionRequest(course.getCourse(), currentTerm, course));
                    if(removeResponse.getStatus() != MessageStatus.SUCCESS) {return;}
                    
                    Message<AddSectionResponse> addResponse = guiService.sendAndWait(MessageType.ADMIN_ADD_COURSE,
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
                    new nPanelPlainText("Are you sure you want to delete " + course.getName() + " ?", UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10, false);
                panel.setPadding(5, 7);
                nPanelModal modal = new nPanelModal((nFrame)frame, panel, w, h);
                enrollButton.addActionListener(ee -> {
                    Message<AdminRemoveSectionResponse> removeResponse = guiService.sendAndWait(MessageType.ADMIN_REMOVE_COURSE,
                            MessageStatus.REQUEST, new AdminRemoveSectionRequest(course.getCourse(), currentTerm, course));
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
            g2d.drawString(course.getCourse().getNumber() + " - " + course.getName(),
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Instructor + time
            g2d.setFont(UITheme.FONT_BODY);
            g2d.setColor(UITheme.TEXT_MUTED);
            String meettimes = "Meet Times |";
            for(MeetTime m : course.getMeetTimes()) {
                meettimes += m.getDay() + "|";
            }
            g2d.drawString(meettimes, //fix when getting access to Section
                    x, y + g2d.getFontMetrics().getAscent());
            
            y += g2d.getFontMetrics().getHeight();
            // Location
            g2d.drawString(course.getInstructor(),x, y + g2d.getFontMetrics().getAscent());
            g2d.setRenderingHints(oldHints);
        }
    }
    public static class DropItemPanel extends nPanel {
        private Section course;
        private nButton[] buttonList;
        private Term currentTerm;
        DropItemPanel(Section _course, Term currentTerm, IAppGUIService guiService) {
            setName("CourseItemPanel");
            this.currentTerm = currentTerm;
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton DropButton = new nButton("Drop");
            DropButton.setBackgroundColor(UITheme.SUCCESS);
            add(DropButton);
            DoDropButton(DropButton, guiService);
            buttonList = new nButton[1];
            buttonList[0] = DropButton;
            doLayout();
        }
        private void DoDropButton(nButton DropButton, IAppGUIService guiService){
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
            String meettimes = "Meet Times |";
            for(MeetTime m : course.getMeetTimes()) {
                meettimes += m.getDay() + "|";
            }
            g2d.drawString(meettimes, //fix when getting access to Section
                    x, y + g2d.getFontMetrics().getAscent());
            
            y += g2d.getFontMetrics().getHeight();
            // Location
            g2d.drawString(course.getInstructor(),x, y + g2d.getFontMetrics().getAscent());
            g2d.setRenderingHints(oldHints);
        }
    }
    public static class WaitlistItemPanel extends nPanel {
        private StudentScheduleItem course;
        private nButton[] buttonList;
        WaitlistItemPanel(StudentScheduleItem course, IAppGUIService guiService) {
            setName("CourseItemPanel");
            this.course = course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton DropButton = new nButton("Unqueue");
            DropButton.setBackgroundColor(UITheme.SUCCESS);
            add(DropButton);
            DoDropButton(DropButton, guiService);
            buttonList = new nButton[1];
            buttonList[0] = DropButton;
            doLayout();
        }
        private void DoDropButton(nButton DropButton, IAppGUIService guiService){
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
                    new nPanelPlainText("Are you sure you want to drop this course?", UITheme.TEXT_PRIMARY),
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
            String meettimes = "Meet Times |";
            for(MeetTime m : course.getSection().getMeetTimes()) {
                meettimes += m.getDay() + "|";
            }
            g2d.drawString(meettimes, //fix when getting access to Section
                    x, y + g2d.getFontMetrics().getAscent());
            
            y += g2d.getFontMetrics().getHeight();
            // Location
            g2d.drawString(course.getSection().getInstructor(),x, y + g2d.getFontMetrics().getAscent());
            g2d.setRenderingHints(oldHints);
        }
    }

}
