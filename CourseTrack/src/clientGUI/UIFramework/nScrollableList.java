package clientGUI.UIFramework;
import java.awt.*;
import java.awt.event.*;
import clientGUI.UIInformations.*;

import global.Log;

public class nScrollableList extends nPanel implements MouseWheelListener {

    private final UIArrayList<nPanel> items = new UIArrayList<nPanel>();
    private int scrollOffset = 0;
    private int itemSpacing = 8;
    private int padding = 8;
    private int contentHeight = 0;

    private int cornerRadius = 16;

    public nScrollableList(UIArrayList<nPanel> panels) {
        setName("ScrollableList");

        setLayout(null);
        setOpaque(false);
        if (panels != null) {
            panels.foreach(p -> addItem(p));
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
        this(new UIArrayList<nPanel>(panels));
    }

    public nScrollableList() {
        this((UIArrayList<nPanel>) null);
    }

    public void addItem(nPanel panel) {
        if (panel == null)
            return;
        
        items.append(panel);
        add(panel);

        panel.addMouseWheelListener(this);

        layoutItems();
    }

    public void removeItem(nPanel panel) {
        if (panel == null)
            return;
        panel.removeMouseWheelListener(this);
        items.remove(panel);
        remove(panel);
        layoutItems();
    }

    public void clearItems() {
        items.foreach(p -> {remove(p); p.removeMouseWheelListener(this);});
        items.clear();
        scrollOffset = 0;
        layoutItems();
    }
    
    public void forEach(UIArrayList.forEachAction<nPanel> action) {
        items.foreach(action);
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

    private int y;
    private int yContent;
    public void layoutItems() {
        int w = getWidth();

        // 1. First pass: compute contentHeight WITHOUT using scrollOffset
        yContent = padding;
        items.foreach(p -> {
            if (p != null) {
                Dimension pref = p.getPreferredSize();
                int h = (pref != null && pref.height > 0) ? pref.height : 40;
                yContent += h + itemSpacing;
            }
        });
        contentHeight = Math.max(0, yContent - padding);

        // 2. Clamp scrollOffset to [0, maxOffset] based on fresh contentHeight
        int maxOffset = Math.max(0, contentHeight + padding - getHeight());
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxOffset) scrollOffset = maxOffset;

        // 3. Second pass: actually place items using clamped scrollOffset
        y = padding - scrollOffset;
        items.foreach(p -> {
            if (p != null) {
                Dimension pref = p.getPreferredSize();
                int h = (pref != null && pref.height > 0) ? pref.height : 40;
                p.setBounds(padding, y, w - padding * 2, h);
                y += h + itemSpacing;
            }
        });

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
        Log.Msg("wheel on " + e.getSource());
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
