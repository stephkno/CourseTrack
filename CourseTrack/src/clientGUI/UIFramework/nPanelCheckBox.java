package clientGUI.UIFramework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class nPanelCheckBox extends nPanel {
    
    private CheckBox cb = null;
    
    public nPanelCheckBox(Dimension size) {
        setName("nPanelCheckBoxArea");
        setSize(size);
        cb = new CheckBox(size);
        add(cb);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int sideLength = Math.min(getWidth(), getHeight());
                cb.setSize(sideLength, sideLength);
                cb.setLocation((int) (getWidth()/2 - cb.getWidth()/2), (int) (getHeight()/2 - cb.getHeight()/2));
                repaint();
            }
        });
        
    }

    public void setColor(Color c) {
        cb.drawColor = c;
    }

    public nPanelCheckBox() {
        this(new Dimension(50, 50));
    }

    public boolean getState() {
        return cb.checked;
    }

    ///*
    // nesting the actual checkbox inside of nPanelCheckBox
    // so when resizing nPanelCheckBox, the checkBox always remains a square
    //*/
    private static class CheckBox extends nPanel {
        public boolean checked = false;
        public Color drawColor = Color.BLACK;
        public CheckBox(Dimension size) {

            setName("nPanelCheckBox");
            int sideLength = (int)Math.min(size.getWidth(), size.getHeight());
            setLocation((int)(size.getWidth()/2), (int)(size.getHeight()/2));
            setSize(sideLength, sideLength);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    checked = !checked;
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            


            Dimension size = getSize();


            Stroke oldStroke = g2d.getStroke();
            
            BasicStroke newStroke = new BasicStroke(3);
            g2d.setStroke(newStroke);

            g2d.setColor(drawColor);
            Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight());
            g2d.draw(rect);

            g2d.setStroke(oldStroke);

            if(checked) {
                int pad = 4;

                g2d.setColor(drawColor);
                rect = new Rectangle2D.Double(pad, pad, size.getWidth()-pad*2, size.getHeight()-pad*2);
                g2d.fill(rect);

            }


        }
    }

}
