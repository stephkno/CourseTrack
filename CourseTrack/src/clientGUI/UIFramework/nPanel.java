package clientGUI.UIFramework;
import javax.swing.JPanel;

import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;


public class nPanel extends JPanel {

    public nPanel() {
        super();

        setName("nPanel");
        setFont(UITheme.FONT_BODY);
        setOpaque(false);
    }


    public enum CrossStringAlignments {
        CENTER,
        TOP,
        BOTTOM
    }


    public enum MainStringAlignments {
        CENTER,
        LEFT,
        RIGHT
    }

    protected double getTextHeight(Graphics2D g) {
        return g.getFontMetrics().getHeight();
    }

    protected double getTextWidth(Graphics2D g, String text) {
        return g.getFontMetrics().stringWidth(text);
    }


    protected double drawRotatedAlignedString(Graphics2D g,String text,double x,double y,double degRot,CrossStringAlignments crossAlignment,MainStringAlignments mainAlignment) {
        if (text == null) { return 0.0; }

        AffineTransform original = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(degRot));

        double width = drawAlignedString(g, text, 0.0, 0.0, crossAlignment, mainAlignment);

        g.setTransform(original);
        return width;
    }

    protected double drawScaledAlignedString(Graphics2D g,String text,double x,double y,double scaleX,double scaleY,CrossStringAlignments crossAlignment, MainStringAlignments mainAlignment) {
        if (text == null) { return 0.0; }

        AffineTransform original = g.getTransform();
        g.translate(x, y);
        g.scale(scaleX, scaleY);

        double width = drawAlignedString(g, text, 0.0, 0.0, crossAlignment, mainAlignment);

        g.setTransform(original);
        return width;
    }

    protected double drawAlignedString(Graphics2D g,String text, double x, double y, CrossStringAlignments crossAlignment, MainStringAlignments mainAlignment) {
        if (text == null) { return 0.0; }

        FontMetrics metrics = g.getFontMetrics();
        double textWidth = metrics.stringWidth(text);
        double textHeight = metrics.getHeight();

        double drawX = x;
        double drawY = y;

        switch (crossAlignment) {
            case CENTER -> drawY += textHeight / 2.0;
            case TOP -> drawY += textHeight;
            case BOTTOM -> { }
        }

        switch (mainAlignment) {
            case CENTER -> drawX -= textWidth / 2.0;
            case LEFT -> { }
            case RIGHT -> drawX -= textWidth;
        }

        g.drawString(text, (float) drawX, (float) drawY);
        return textWidth;
    }

    public static class nDocument {
        private final StringBuilder text;
        private int cursorIndex;

        public nDocument() {
            this("");
        }

        public nDocument(String startingString) {
            text = new StringBuilder(startingString != null ? startingString : "");
            cursorIndex = text.length();
        }

        public String getText() {
            return text.toString();
        }

        public void setText(String s) {
            text.setLength(0);
            if (s != null) {
                text.append(s);
            }
            cursorIndex = Math.min(cursorIndex, text.length());
        }

        public int length() {
            return text.length();
        }

        public int getCursorIndex() {
            return cursorIndex;
        }

        public void setCursorIndex(int index) {
            cursorIndex = Math.max(0, Math.min(index, text.length()));
        }

        public void insert(char c) {
            text.insert(cursorIndex, c);
            cursorIndex++;
        }

        public void remove(int index) {
            if (index >= 0 && index < text.length()) {
                text.deleteCharAt(index);
                if (cursorIndex > index) {
                    cursorIndex--;
                }
            }
        }

        public void removeBeforeCaret() {
            if (cursorIndex > 0) {
                text.deleteCharAt(cursorIndex - 1);
                cursorIndex--;
            }
        }

        public void moveLeft() {
            if (cursorIndex > 0)
                cursorIndex--;
        }

        public void moveRight() {
            if (cursorIndex < text.length())
                cursorIndex++;
        }
    }
}
