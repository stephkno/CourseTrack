package clientGUI.PageComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

import clientGUI.UIFramework.*;
import clientGUI.UIInformations.*;

public class Panels {
    public static class CourseItemPanel extends nPanel {
        private final UICourseInfo course;
        private nButton[] buttonList;
        CourseItemPanel(UICourseInfo _course, UserRole role) {
            course = _course;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(200, 70));

            
            switch (role) {
                case UserRole.admin:
                    nButton editButton = new nButton("Edit");
                    nButton deleteButton = new nButton("Delete");
                    editButton.setBackgroundColor(UITheme.SUCCESS);
                    deleteButton.setBackgroundColor(UITheme.FAIL);
                    add(editButton);
                    add(deleteButton);
                    buttonList = new nButton[2];
                    buttonList[1] = editButton; buttonList[0] = deleteButton;
                    break;
            
                default:
                    nButton enrollButton = new nButton("Enroll");
                    enrollButton.setBackgroundColor(UITheme.SUCCESS);
                    add(enrollButton);
                    DoEnrollButton(enrollButton);
                    buttonList = new nButton[1];
                    buttonList[0] = enrollButton;
                    break;
            }
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
                nFrame.GridLayout lowerOptions = new nFrame.GridLayout((nFrame)frame, options);
                lowerOptions.setGridSize(2, 1);
                
                Component[] enrollPanel = {
                    new JPanelPlainText(course.code, UITheme.TEXT_PRIMARY),
                    new JPanelPlainText(course.title, UITheme.TEXT_PRIMARY),
                    new JPanelPlainText(course.instructor, UITheme.TEXT_PRIMARY),
                    new JPanelPlainText(course.time, UITheme.TEXT_PRIMARY),
                    new JPanelPlainText(course.location, UITheme.TEXT_PRIMARY),
                    lowerOptions
                };
                nFrame.ListLayout panel = new nFrame.ListLayout((nFrame)frame, enrollPanel, new Dimension(100, 100), 10, 10);
                nModal modal = new nModal(frame, panel, x, y, w, h);
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

            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            try {
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(UITheme.BG_APP);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);

                g2.setColor(UITheme.BG_ELEVATED_ELEVATED);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);

                int x = 12;
                int y = 10;

                // Title line
                g2.setFont(UITheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
                g2.setColor(UITheme.TEXT_PRIMARY);
                g2.drawString(course.code + " - " + course.title,
                        x, y + g2.getFontMetrics().getAscent());
                y += g2.getFontMetrics().getHeight();

                // Instructor + time
                g2.setFont(UITheme.FONT_BODY);
                g2.setColor(UITheme.TEXT_MUTED);
                g2.drawString(course.instructor + " | " + course.time,
                        x, y + g2.getFontMetrics().getAscent());
                y += g2.getFontMetrics().getHeight();

                // Location
                g2.drawString(course.location,
                        x, y + g2.getFontMetrics().getAscent());
            } finally {
                g2.dispose();
            }
        }
    }
}
