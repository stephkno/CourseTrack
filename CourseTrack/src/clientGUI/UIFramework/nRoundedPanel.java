package clientGUI.UIFramework;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class nRoundedPanel extends nPanel {
    private Color backgroundColor = new Color(23, 26, 45);
    private Color borderColor = new Color(255, 255, 255, 40);
    private int radius = 20;

    public nRoundedPanel() {
        setOpaque(false);
    }

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
        repaint();
    }

    public void setBorderColor(Color c) {
        this.borderColor = c;
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = Math.max(0, radius);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        Shape rr = new RoundRectangle2D.Double(
                0, 0, w - 1, h - 1, radius, radius);
        g2d.setColor(backgroundColor);
        g2d.fill(rr);
        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.draw(rr);
        }
        g2d.setRenderingHints(oldHints);
    }
}
