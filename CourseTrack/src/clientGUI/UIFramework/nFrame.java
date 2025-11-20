package clientGUI.UIFramework;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class nFrame extends JFrame {
    private final Dimension baseSize;
    private final List<TrackedChild> trackedChildren = new ArrayList<>();

    private static class TrackedChild {
        final Component comp;
        final Rectangle initialBounds;

        TrackedChild(Component comp) {
            this.comp = comp;
            this.initialBounds = comp.getBounds();
        }
    }

    public nFrame(String title, int width, int height) {
        super(title);

        baseSize = new Dimension(width, height);
        setSize(baseSize);
        setLocationRelativeTo(null);


        setLayout(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeChildren();
            }
        });
    }

    @Override
    public Component add(Component comp) {
        Component c = super.add(comp);
        trackedChildren.add(new TrackedChild(c));
        return c;
    }

    private void resizeChildren() {
        if (baseSize.width == 0 || baseSize.height == 0)
            return;

        double sx = getWidth() / (double) baseSize.width;
        double sy = getHeight() / (double) baseSize.height;

        for (TrackedChild t : trackedChildren) {
            Rectangle r = t.initialBounds;
            int newX = (int) Math.round(r.x * sx);
            int newY = (int) Math.round(r.y * sy);
            int newW = (int) Math.round(r.width * sx);
            int newH = (int) Math.round(r.height * sy);
            t.comp.setBounds(newX, newY, newW, newH);
        }


        revalidate();
        repaint();
    }

    public static class ListLayout extends nPanel {

        public enum Direction {
            UP,
            DOWN,
            LEFT,
            RIGHT
        }

        public enum Style {
            NONE,
            BORDER
        }

        private Direction direction = Direction.UP;
        private Style style = Style.NONE;
        private int paddingX = 0;
        private int paddingY = 0;
        public Color backgroundColor = new Color(0, 0, 0, 0);

        private int cornerRadius = 20;

        public void setCornerRadius(int radius) {
            this.cornerRadius = Math.max(0, radius);
            repaint();
        }

        public ListLayout(nFrame frame,Component[] components,Dimension size,int xPos,int yPos) {
            setLayout(null);
            setOpaque(false);
            setSize(size);
            setLocation(xPos, yPos);

            if (components != null) {
                for (Component c : components) {
                    if (c != null) {
                        add(c);
                    }
                }
            }

            frame.add(this);
            layoutChildren();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutChildren();
                }
            });
        }
        public ListLayout(nFrame frame,Component[] components) {
            setLayout(null);
            setOpaque(false);
            setSize(new Dimension(10, 10));
            setLocation(10, 10);

            if (components != null) {
                for (Component c : components) {
                    if (c != null) {
                        add(c);
                    }
                }
            }

            frame.add(this);
            layoutChildren();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutChildren();
                }
            });
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
            layoutChildren();
        }

        public void setStyle(Style style) {
            this.style = style;
            repaint();
        }

        public void setPadding(int paddingX, int paddingY) {
            this.paddingX = paddingX;
            this.paddingY = paddingY;
            layoutChildren();
        }

        private void layoutChildren() {
            Component[] children = getComponents();
            if (children.length == 0)
                return;

            int w = getWidth();
            int h = getHeight();
            int n = children.length;

            if (direction == Direction.UP || direction == Direction.DOWN) {
                int availableHeight = h - paddingY * (n + 1);
                int childHeight = Math.max(0, availableHeight / n);
                int childWidth = w - paddingX * 2;

                int y = paddingY;
                for (int i = 0; i < n; i++) {
                    int index = (direction == Direction.UP) ? i : (n - 1 - i);
                
                    int hChild = (i == n - 1)
                            ? (h - paddingY - y)
                            : childHeight;

                    children[index].setBounds(paddingX, y, childWidth, hChild);
                    y += hChild + paddingY;
                }
            } else {
                int availableWidth = w - paddingX * (n + 1);
                int childWidth = Math.max(0, availableWidth / n);
                int childHeight = h - paddingY * 2;

                int x = paddingX;
                for (int i = 0; i < n; i++) {
                    int index = (direction == Direction.LEFT) ? i : (n - 1 - i);

                    int wChild = (i == n - 1)
                            ? (w - paddingX - x)
                            : childWidth;

                    children[index].setBounds(x, paddingY, wChild, childHeight);
                    x += wChild + paddingX;
                }
            }

            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints oldHints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
            if (style == Style.BORDER) {
                g2d.setColor(Color.GRAY);
                g2d.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
            }
            g2d.setRenderingHints(oldHints);
        }
    }

    public static class GridLayout extends nPanel {

        private final Component[] componentList;
        private int cols;
        private int rows;
        private int padding = 0;

        public GridLayout(nFrame frame,Component[] components,Dimension size,int xPos,int yPos) {
            this.componentList = (components != null) ? components : new Component[0];
            this.cols = Math.max(1, this.componentList.length);
            this.rows = 1;

            setLayout(null);
            setSize(size);
            setLocation(xPos, yPos);
            setOpaque(false);

            for (Component c : componentList) {
                if (c != null) {
                    add(c);
                }
            }

            frame.add(this);
            layoutChildren();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutChildren();
                }
            });
        }
        public GridLayout(nFrame frame,Component[] components) {
            this.componentList = (components != null) ? components : new Component[0];
            this.cols = Math.max(1, this.componentList.length);
            this.rows = 1;

            setLayout(null);
            setSize(new Dimension(10, 10));
            setLocation(10, 10);
            setOpaque(false);

            for (Component c : componentList) {
                if (c != null) {
                    add(c);
                }
            }

            frame.add(this);
            layoutChildren();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutChildren();
                }
            });
        }

        public void setPadding(int padding) {
            this.padding = padding;
            layoutChildren();
        }

        public void setGridSize(int cols, int rows) {
            this.cols = Math.max(1, cols);
            this.rows = Math.max(1, rows);
            layoutChildren();
        }

        private void layoutChildren() {
            if (componentList.length == 0)
                return;

            int w = getWidth();
            int h = getHeight();

            int cellWidthBase = (w - padding * (cols + 1)) / cols;
            int cellHeightBase = (h - padding * (rows + 1)) / rows;

            int index = 0;
            for (int row = 0; row < rows; row++) {
                // last row stretches to bottom if needed
                int y = padding + row * (cellHeightBase + padding);
                int cellHeight = (row == rows - 1)
                        ? (h - padding - y)
                        : cellHeightBase;

                for (int col = 0; col < cols; col++) {
                    if (index >= componentList.length)
                        return;

                    int x = padding + col * (cellWidthBase + padding);
                    int cellWidth = (col == cols - 1)
                            ? (w - padding - x)
                            : cellWidthBase;

                    Component c = componentList[index++];
                    if (c == null)
                        continue;

                    c.setBounds(x, y, cellWidth, cellHeight);
                }
            }

            revalidate();
            repaint();
        }

    }
}
