package clientGUI.UIFramework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class nPanelModal extends nPanel {

    private final nFrame frame;
    private final JComponent glass;
    private final Component content;
    private final int contentWidth;
    private final int contentHeight;

    public nPanelModal(nFrame frame, Component content, int contentWidth, int contentHeight) {
        this.frame = frame;
        this.glass = (JComponent) frame.getGlassPane();
        this.content = content;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;

        setOpaque(false);
        setLayout(null);

        add(content);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                Rectangle r = content.getBounds();

                if (!r.contains(p)) {
                    close();
                }

            }
        });

        addMouseMotionListener(new MouseAdapter() {
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeToFrame();
            }
        });

        install();
    }

    private void install() {
        glass.setLayout(null);
        glass.setVisible(true);


        resizeToFrame();

        glass.add(this);
        glass.revalidate();
        glass.repaint();

        content.setFocusable(true);
        content.requestFocusInWindow();
    }

    private void resizeToFrame() {
        int fw = frame.getWidth();
        int fh = frame.getHeight();
        setBounds(0, 0, fw, fh);

        int x = (fw - contentWidth) / 2;
        int y = (fh - contentHeight) / 2;
        content.setBounds(x, y, contentWidth, contentHeight);

        revalidate();
        repaint();
    }

    public void close() {
        glass.remove(this);
        glass.revalidate();
        glass.repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        
        Point contentLocation = content.getLocation();
        Rectangle2D.Double rect = new Rectangle2D.Double(contentLocation.getX(), contentLocation.getY(), contentWidth, contentHeight);
        g2d.setColor(UITheme.BG_ELEVATED);
        g2d.fill(rect);
        g2d.setColor(UITheme.TEXT_PRIMARY);
        g2d.draw(rect);
        
    }
}
