package clientGUI.PageComponents;

import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;
import global.data.Course;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import javax.swing.JFrame;

public class Panels {
    public static class CourseItemPanel extends nPanel {
        private final Course course;
        private nButton[] buttonList;
        CourseItemPanel(Course _course, UserRole role) {
            setName("CourseItemPanel");

            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            
            switch (role) {
                case admin -> {
                    nButton editButton = new nButton("Edit");
                    nButton deleteButton = new nButton("Delete");
                    editButton.setBackgroundColor(UITheme.SUCCESS);
                    deleteButton.setBackgroundColor(UITheme.FAIL);
                    add(editButton);
                    DoEditButton(editButton);
                    add(deleteButton);
                    buttonList = new nButton[2];
                    buttonList[1] = editButton; buttonList[0] = deleteButton;
                }
            
                default -> {
                    nButton enrollButton = new nButton("Enroll");
                    enrollButton.setBackgroundColor(UITheme.SUCCESS);
                    add(enrollButton);
                    DoEnrollButton(enrollButton);
                    buttonList = new nButton[1];
                    buttonList[0] = enrollButton;
                }
            }
            doLayout();
        }
        private void DoEnrollButton(nButton EnrollButton){
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
                    modal.close();
                });
                cancelButton.addActionListener(ee -> {
                    modal.close();
                });
            });
        
        }
        private void DoEditButton(nButton EditButton){
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
                Component[] enrollPanel = {
                    new nPanelPlainText("Edit Course", UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Campus", UITheme.TEXT_PRIMARY),
                    new nPanelTextBox(course.getDepartment().getCampus().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Department", UITheme.TEXT_PRIMARY),
                    new nPanelTextBox(course.getDepartment().getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Course", UITheme.TEXT_PRIMARY),
                    new nPanelTextBox(course.getName(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Course Number", UITheme.TEXT_PRIMARY),
                    new nPanelTextBox(""+course.getNumber(), UITheme.TEXT_PRIMARY),
                    new nPanelPlainText("Course Units", UITheme.TEXT_PRIMARY),
                    new nPanelTextBox(""+course.getUnits(), UITheme.TEXT_PRIMARY),
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
            g2d.drawString("PUT INSTRUCTOR NAE" + " | " + "PUT COURSE TIME", //fix when getting access to Section
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Location
            g2d.drawString("PUT LOCATION HERE",x, y + g2d.getFontMetrics().getAscent());
            g2d.setRenderingHints(oldHints);
        }
    }
    public static class DropItemPanel extends nPanel {
        private final Course course;
        private nButton[] buttonList;
        DropItemPanel(Course _course, UserRole role) {
            setName("CourseItemPanel");

            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            nButton DropButton = new nButton("Drop");
            DropButton.setBackgroundColor(UITheme.SUCCESS);
            add(DropButton);
            DoDropButton(DropButton);
            buttonList = new nButton[1];
            buttonList[0] = DropButton;
            doLayout();
        }
        private void DoDropButton(nButton DropButton){
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
            g2d.drawString("PUT INSTRUCTOR NAE" + " | " + "PUT COUSE TIME", //fix when getting access to Section
                    x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
            // Location
            g2d.drawString("PUT LOCATION HERE",x, y + g2d.getFontMetrics().getAscent());
            g2d.setRenderingHints(oldHints);
        }
    }
}
