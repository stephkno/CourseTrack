package clientGUI.UIFramework;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class nScrollableList extends nPanel implements MouseWheelListener {

    private final List<nPanel> items = new ArrayList<>();
    private int scrollOffset = 0;
    private int itemSpacing = 8;
    private int padding = 8;
    private int contentHeight = 0;

    private int cornerRadius = 16;

    public nScrollableList(List<nPanel> panels) {
        setLayout(null);
        setOpaque(false);
        if (panels != null) {
            for (nPanel p : panels) {
                addItem(p);
            }
        }
        addMouseWheelListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutItems();
            }
        });
    }

    public nScrollableList(nPanel[] panels) {
        this(panels != null ? java.util.Arrays.asList(panels) : null);
    }

    public nScrollableList() {
        this((List<nPanel>) null);
    }

    public void addItem(nPanel panel) {
        if (panel == null)
            return;
        items.add(panel);
        add(panel);
        layoutItems();
    }

    public void removeItem(nPanel panel) {
        if (panel == null)
            return;
        items.remove(panel);
        remove(panel);
        layoutItems();
    }

    public void clearItems() {
        for (nPanel p : items) {
            remove(p);
        }
        items.clear();
        scrollOffset = 0;
        layoutItems();
    }

    public void setItemSpacing(int spacing) {
        this.itemSpacing = Math.max(0, spacing);
        layoutItems();
    }

    public void setInnerPadding(int padding) {
        this.padding = Math.max(0, padding);
        layoutItems();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = Math.max(0, radius);
        repaint();
    }

    private void layoutItems() {
        int w = getWidth();
        int y = padding - scrollOffset;

        for (nPanel p : items) {
            if (p == null)
                continue;

            Dimension pref = p.getPreferredSize();
            int h = (pref != null && pref.height > 0)
                    ? pref.height
                    : 40;

            p.setBounds(padding, y, w - padding * 2, h);
            y += h + itemSpacing;
        }

        contentHeight = y + scrollOffset - padding;
        clampScroll();
        repaint();
    }

    private void clampScroll() {
        int maxOffset = Math.max(0, contentHeight + padding - getHeight());
        if (scrollOffset < 0)
            scrollOffset = 0;
        if (scrollOffset > maxOffset)
            scrollOffset = maxOffset;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        int scrollAmount = 20;

        scrollOffset += rotation * scrollAmount;
        clampScroll();
        layoutItems();
    }

    @Override
    public void doLayout() {
        layoutItems();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2d.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        g2d.setRenderingHints(oldHints);
    }
}
