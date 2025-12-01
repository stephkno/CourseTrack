package clientGUI.UIFramework;

import java.awt.*;
import java.awt.event.*;

public class nPanelDropDown extends nButton {

    private boolean expanded = false;

    private int rowHeight = 28;
    private int paddingX = 8;

    private Color backgroundColor = UITheme.BG_ELEVATED;
    private Color borderColor = UITheme.BG_ELEVATED_ELEVATED;
    private Color textColor = UITheme.TEXT_PRIMARY;
    private Color arrowColor = UITheme.TEXT_PRIMARY;

    private final nScrollableList dropDownList = new nScrollableList();

    private int dropDownListHeight = 100;
    private int collapsedHeight = -1;
    private Dimension preferredSz = new Dimension(10, 20);

    nButton selected = null;

    public nButton getSelected() {
        return selected;
    }

    public void clearOptions() {
        selected = null;
        dropDownList.clearItems();
    }

    public nPanelDropDown() {
        setName("nPanelDropDown");
        setOpaque(false);
        setLayout(null);

        add(dropDownList);
        dropDownList.setVisible(false);

        addActionListener(e -> {
            expanded = !expanded;
            dropDownList.setVisible(expanded);
            getParent().setComponentZOrder(this, 0);
            updateSizeForState();
        });
    }

    public nPanelDropDown(nButton[] initialOptions) {
        this();
        if (initialOptions == null) {
            return;
        }
        for (nButton b : initialOptions) {
            if (b != null) {
                addOption(b);
            }
        }
    }

    public nPanelDropDown(int cardWidth, int cardHeight) {
        this();
        preferredSz = new Dimension(cardWidth, cardHeight);
    }

    public void setOptionSize(Dimension d) {
        preferredSz = d;
    }

    public void setDropDownHeight(int height) {
        dropDownListHeight = height;
    }

    @Override
    public void setSize(int w, int h) {

        if (!expanded) {
            collapsedHeight = h;
        } else if (collapsedHeight < 0) {
            collapsedHeight = h;
        }
        int newHeight = expanded ? collapsedHeight + dropDownListHeight : collapsedHeight;
        super.setSize(w, newHeight);
        layoutDropDownList();
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {

        if (!expanded) {
            collapsedHeight = h;
        } else if (collapsedHeight < 0) {
            collapsedHeight = h;
        }

        int newHeight = expanded ? collapsedHeight + dropDownListHeight : collapsedHeight;
        super.setBounds(x, y, w, newHeight);

        layoutDropDownList();
    }

    private void layoutDropDownList() {
        int w = getWidth();
        if (preferredSz != null) {
            preferredSz = new Dimension(w, (int) preferredSz.getHeight());
        } else {
            preferredSz = new Dimension(w, 20);
        }
        if (collapsedHeight < 0) {
            collapsedHeight = Math.max(rowHeight, getHeight());
        }
        dropDownList.setBounds(0, collapsedHeight, w, dropDownListHeight);
        dropDownList.forEach(p -> {
            p.setPreferredSize(preferredSz);
        });
        dropDownList.layoutItems();
    }

    private void updateSizeForState() {
        int w = getWidth();
        int h = getHeight();
        if (collapsedHeight < 0) {
            collapsedHeight = h;
        }



        setSize(w, h);
        revalidate();
        repaint();

    }

    public void addOption(nButton newOption) {
        if (newOption == null) {
            return;
        }
        if (preferredSz != null) {
            newOption.setPreferredSize(preferredSz);
        } else {
            newOption.setPreferredSize(new Dimension(getWidth(), 20));
        }
        newOption.addActionListener(e -> {
            selected = newOption;
        });
        if (selected == null) {
            selected = newOption;
        }
        dropDownList.addItem(newOption);
        dropDownList.layoutItems();
        revalidate();
        repaint();
    }

    public void setRowHeight(int h) {
        rowHeight = Math.max(16, h);
        repaint();
    }

    public void setBackgroundColor(Color c) {
        if (c != null) {
            backgroundColor = c;
            repaint();
        }
    }

    public void setBorderColor(Color c) {
        if (c != null) {
            borderColor = c;
            repaint();
        }
    }

    public void setTextColor(Color c) {
        if (c != null) {
            textColor = c;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int headerHeight = (collapsedHeight > 0) ? collapsedHeight : rowHeight;

        // background + border
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, w - 1, headerHeight - 1, 8, 8);
        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, w - 1, headerHeight - 1, 8, 8);

        // arrow
        int arrowSize = 6;
        int cx = w - paddingX - arrowSize;
        int cy = headerHeight / 2;

        Polygon tri = new Polygon();
        if (expanded) {
            tri.addPoint(cx - arrowSize, cy + arrowSize / 2);
            tri.addPoint(cx + arrowSize, cy + arrowSize / 2);
            tri.addPoint(cx, cy - arrowSize / 2);
        } else {
            tri.addPoint(cx - arrowSize, cy - arrowSize / 2);
            tri.addPoint(cx + arrowSize, cy - arrowSize / 2);
            tri.addPoint(cx, cy + arrowSize / 2);
        }
        g2d.setColor(arrowColor);
        g2d.fillPolygon(tri);

        String label = selected == null ? "" : selected.getText();
        if (label != null && !label.isEmpty()) {
            g2d.setColor(textColor);
            FontMetrics fm = g2d.getFontMetrics();

            int textX = paddingX;
            int textY = (headerHeight - fm.getHeight()) / 2 + fm.getAscent();

            int maxTextWidth = cx - paddingX * 2;
            if (fm.stringWidth(label) > maxTextWidth) {
                while (label.length() > 0 &&
                        fm.stringWidth(label + "...") > maxTextWidth) {
                    label = label.substring(0, label.length() - 1);
                }
                label += "...";
            }

            g2d.drawString(label, textX, textY);
        }

        g2d.setRenderingHints(oldHints);
    }

}
