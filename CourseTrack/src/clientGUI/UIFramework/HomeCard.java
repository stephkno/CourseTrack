package clientGUI.UIFramework;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class HomeCard extends nPanel {

    private String title;
    private String description;

    private Color bgColor = new Color(23, 26, 45);
    private Color borderColor = new Color(255, 255, 255, 60);
    private Color titleColor = Color.WHITE;
    private Color textColor = new Color(210, 215, 235);
    private int radius = 20;

    public HomeCard(String title, String description) {
        this.title = title;
        this.description = description;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        Shape rr = new RoundRectangle2D.Double(0, 0, w - 1, h - 1, radius, radius);
        // background
        g2d.setColor(bgColor);
        g2d.fill(rr);
        // border
        g2d.setColor(borderColor);
        g2d.draw(rr);
        int padding = 14;
        int x = padding;
        int y = padding;
        // title
        g2d.setFont(UITheme.FONT_H3);
        g2d.setColor(titleColor);
        g2d.drawString(title, x, y + g2d.getFontMetrics().getAscent());
        y += g2d.getFontMetrics().getHeight() + 6;
        // description
        g2d.setFont(UITheme.FONT_BODY);
        g2d.setColor(textColor);
        String[] lines = description.split("\n");
        for (String line : lines) {
            g2d.drawString(line, x, y + g2d.getFontMetrics().getAscent());
            y += g2d.getFontMetrics().getHeight();
        }
        g2d.setRenderingHints(oldHints);
    }
}
