package clientGUI.UIFramework;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class nPanelTextBox extends nPanel {

    private final nDocument document = new nDocument();

    public boolean drawBorder = true;
    public boolean drawUnderline = false;

    public Color textColor = Color.BLACK;
    public Color backgroundColor = new Color(0, 0, 0, 0);

    

    public nPanelTextBox(String initialText) {
        document.setText(initialText);
        setOpaque(false);
        setFocusable(true);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                repaint(); //remove carat if focus lost
            }
        });

        // move carat to mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                
                int index = pointToIndex(e.getX(), e.getY());
                document.setCursorIndex(index);
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // ignore control chars like enter tab etc
                if (!Character.isISOControl(c)) {
                    document.insert(c);
                    repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE -> {
                        document.removeBeforeCaret();
                        repaint();
                    }
                    case KeyEvent.VK_LEFT -> {
                        document.moveLeft();
                        repaint();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        document.moveRight();
                        repaint();
                    }
                }
            }
        });
    }

    public nPanelTextBox() {
        this("");
    }

    public void setText(String _text) {
        document.setText(_text);
        repaint();
    }

    public String getText() {
        return document.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // background
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // text
        g2d.setColor(textColor);
        g2d.setFont(getFont());
        Dimension size = getSize();
        String text = document.getText();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int xStart = (int) ((size.getWidth() - textWidth) / 2.0);
        int baselineY = (int) ((size.getHeight() + fm.getAscent() - fm.getDescent()) / 2.0);
        g2d.drawString(text, xStart, baselineY);

        // caret
        if(isFocusOwner()) {
            int caretX = indexToX(g2d, document.getCursorIndex(), xStart, text);
            int caretTop = baselineY - fm.getAscent();
            int caretBottom = baselineY + fm.getDescent();
            g2d.drawLine(caretX, caretTop, caretX, caretBottom);
        }
        

        if (drawUnderline) {
            int underlineY = baselineY + fm.getDescent() / 2;
            g2d.drawLine(xStart, underlineY, xStart + textWidth, underlineY);
        }
        if (drawBorder) {
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        g2d.setRenderingHints(oldHints);
    }

    //find index for carat x and y pos (mouse x and y)
    private int pointToIndex(int mouseX, int mouseY) {
        String text = document.getText();
        if (text.isEmpty()) { return 0; }

        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text);
        int xStart = (getWidth() - textWidth) / 2;

        if (mouseX <= xStart) { return 0; }
        if (mouseX >= xStart + textWidth) { return text.length(); }

        int x = xStart;
        for (int i = 0; i < text.length(); i++) {
            int cw = fm.charWidth(text.charAt(i));
            int mid = x + cw / 2;
            if (mouseX < mid) { return i; }
            if (mouseX < x + cw) { return i + 1; }
            x += cw;
        }
        return text.length();
    }

    private int indexToX(Graphics2D g2d, int index, int xStart, String text) {
        FontMetrics fm = g2d.getFontMetrics();
        index = Math.max(0, Math.min(index, text.length()));

        int x = xStart;
        for (int i = 0; i < index; i++) {
            x += fm.charWidth(text.charAt(i));
        }
        return x;
    }
}
