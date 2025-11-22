package clientGUI.UIFramework;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("unused")
public class JPanelPlainText extends nPanel {

    private String text = "";
    public boolean drawBorder = false;
    public boolean drawUnderline = false;

    private double scale = 1.0;

    private Double baseArea = null;

    public Color textColor = Color.BLACK;

    private double textHeight = 0;
    private CrossStringAlignments crossAlignment = CrossStringAlignments.CENTER;
    private MainStringAlignments mainAlignment = MainStringAlignments.CENTER;

    public JPanelPlainText() {
        init();
    }

    public JPanelPlainText(String _text) {
        text = _text;
        init();
    }
    public JPanelPlainText(String _text, Color col) {
        text = _text;
        textColor = col;
        init();
    }

    public JPanelPlainText(String _text, Point _location) {
        text = _text;
        setLocation(_location);
        init();
    }

    public JPanelPlainText(String _text, Dimension _size) {
        text = _text;
        setSize(_size);
        init();
    }

    public JPanelPlainText(String _text, Dimension _size, Point _location) {
        text = _text;
        setSize(_size);
        setLocation(_location);
        init();
    }

    private void init() {
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                if (w <= 0 || h <= 0) {return;}

                double area = (double) w * h;
                if (baseArea == null) {
                    baseArea = area;
                }
            }
        });
    }

    public void setText(String _text) {
        text = (_text != null) ? _text : "";
        repaint();
    }

    public String getText() {
        return text;
    }

    public double getTextHeight() {
        return textHeight;
    }

    public void setAlignment(CrossStringAlignments cross, MainStringAlignments main) {
        mainAlignment = main;
        crossAlignment = cross;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (text == null || text.isEmpty()){return;};

        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(textColor);
        Dimension size = getSize();
        double cx = size.getWidth() / 2;// 2.0;
        double cy = size.getHeight() / 2.0; //mainStringAlignment.CENTER
        switch(mainAlignment) {
                case MainStringAlignments.LEFT -> cx = 0;
                case MainStringAlignments.RIGHT -> cx = size.getWidth();
                case MainStringAlignments.CENTER -> {}
            }
            
        // draw the scaled, centered string
        textHeight = g.getFontMetrics().getHeight();
        Rectangle2D textRect = g.getFontMetrics().getStringBounds(text, g2d);
        scale = Math.min(size.getWidth()/textRect.getWidth(), 1);
        drawScaledAlignedString(g2d,text,cx,cy,scale,scale,crossAlignment,mainAlignment);
        if (drawUnderline) {
            int textWidth = fm.stringWidth(text);
            int baselineY = (int) (cy + fm.getAscent() * scale / 2.0);
            int startX = (int) (cx - (textWidth * scale) / 2.0); 
            
            int endX = (int) (startX + textWidth * scale);
            g2d.drawLine(startX, baselineY + 2, endX, baselineY + 2);
        }
        if (drawBorder) {
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        g2d.setRenderingHints(oldHints);
    }
}
