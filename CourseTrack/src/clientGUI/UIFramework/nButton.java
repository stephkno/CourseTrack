package clientGUI.UIFramework;
import java.awt.*;
import java.awt.event.*;

import clientGUI.UIInformations.UIArrayList;


public class nButton extends nRoundedPanel {

    private String text;
    private boolean hovered = false;
    private boolean pressed = false;

    private Color baseColor;

    private final UIArrayList<ActionListener> listeners = new UIArrayList<>();

    public nButton(String text) {
        setName("nButton");

        this.text = text != null ? text : "";
        init();
    }

    public nButton() {
        this("");
    }

    private void init() {
        setRadius(UITheme.RADIUS_SM);
        baseColor = UITheme.BG_ELEVATED;
        super.setBackgroundColor(baseColor);
        setBorderColor(new Color(0, 0, 0, 0));
        setFont(UITheme.FONT_BODY);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateColors();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                updateColors();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isEnabled())
                    return;
                pressed = true;
                updateColors();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isEnabled())
                    return;
                boolean wasPressed = pressed;
                pressed = false;
                updateColors();

                if (wasPressed && contains(e.getPoint())) {
                    fireActionEvent();
                }
            }
        });
    }


    @Override
    public void setBackgroundColor(Color c) {
        baseColor = c;
        super.setBackgroundColor(c);
    }

    private void updateColors() {
        Color base = (baseColor != null) ? baseColor : UITheme.BG_ELEVATED;

        if (!isEnabled()) {
            setBackgroundColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 120));
        } else if (pressed) {
            super.setBackgroundColor(base.darker());
        } else if (hovered) {
            super.setBackgroundColor(base.brighter());
        } else {
            super.setBackgroundColor(base);
        }
        repaint();
    }

    private void fireActionEvent() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
        listeners.foreach(l -> l.actionPerformed(evt));
    }

    public void simulateClick() {
        fireActionEvent();
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
        repaint();
    }

    public String getText() {
        return text;
    }

    public void addActionListener(ActionListener l) {
        if (l != null && !listeners.contains(l)) {
            listeners.append(l);
        }
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(getFont());
        g2d.setColor(UITheme.TEXT_PRIMARY);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        // center horizontally
        int x = (getWidth() - textWidth) / 2;
        // vertical center: box center minus half text height, then add ascent
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.setRenderingHints(oldHints);
    }
}
