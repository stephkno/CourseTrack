package clientGUI.UIFramework;
import java.awt.Color;
import java.awt.Component;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.event.MouseEvent;

public class nModal extends nPanel {
    JFrame frame = null;
    Component component = null;
    nModal self = this;
    //Component will just be drawn ontop of the nModal
    public nModal(JFrame _frame, Component _component, int x, int y, int width, int height){
        frame = _frame;
        component = _component;

        setOpaque(true);
        setLayout(null);

        setBackground(new Color(0, 0, 0, 180));

        
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                component.setBounds(x, y, width, height);
                repaint();
                System.out.println(component);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point mp = e.getPoint();
                double mx = mp.getX();
                double my = mp.getY();
                Rectangle bounds = component.getBounds();
                System.out.println(bounds.toString());
                System.out.println(mp.toString());
                if((mx < bounds.getX() || mx > bounds.getX() + bounds.getWidth()) || (my < bounds.getY() || my > bounds.getY()+bounds.getHeight())) {
                    close();
                }
            }
        });

        

        JComponent pane = (JComponent) frame.getGlassPane();
        pane.setVisible(true);
        pane.setLayout(null);
        

        setBounds(0, 0, pane.getWidth(), pane.getHeight());

        add(component);
        component.setBounds(x, y, width, height);

        pane.add(self);
        pane.revalidate();
        pane.repaint();
    }

    public void close() {
        JComponent pane = (JComponent) frame.getGlassPane();
        System.out.println("Closing modal");
        component = null;
        pane.remove(self);
        pane.revalidate();
        pane.repaint();
        frame = null;
    }
}
