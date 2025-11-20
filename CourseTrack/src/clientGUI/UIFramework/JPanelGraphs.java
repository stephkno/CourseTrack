package clientGUI.UIFramework;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.geom.*;

@SuppressWarnings("unused")
public class JPanelGraphs extends nPanel {

    public enum GraphTypes {
        BAR,
        LINE
    }

    /*
     * Object that JPanelGraph interprets into data used to draw graphs
     * There can only be one x axis, but multiple y axis', allowing for graphs that
     * can help compare different data
     * Variable Color[] colors is the array of colors that are used if there are
     * more than one y axis on one graph to help differentiate
     * colors is treated similarly to a linked list in code, where you can
     * infinitely traverse through the list in one direction. colors[index %
     * colors.length]
     */
    public static class GraphData {
        public double[] xAxis;
        public double[][] yAxis;
        public int length = 0;
        private int ind = 0;

        public GraphData(double[] x, double[]... y) {// make version for string, double etc
            xAxis = x;
            yAxis = new double[y.length][];
            ind = 0;
            for (double[] i : y) {
                yAxis[ind] = i;
                if (i.length > length) {
                    length = i.length;
                }
                ind++;
            }
        }

        public Color[] colors = { Color.BLACK, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.YELLOW };

    }

    // #region helper functions
    /*
     * Finds the most significant digit in double n
     * e.g mostSignificantDigit(0.0056) = 5, mostSignificantDigit(1.0005) = 1
     * (Just the leftmost digit that's not 0)
     */
    private double mostSignificantDigit(double n) {
        String nString = String.valueOf(n);
        char[] charArray = nString.toCharArray();
        if (n >= 1) {
            return Character.digit(charArray[0], 10);
        } // if its greater than one, the first digit is the most significant digit
        if (nString.contains("E")) { // if its in scientific notation then handle it differently
            String rString = "";
            String[] splits = nString.split("E");
            char[] charArray0 = splits[0].toCharArray(); // what comes before E
            char[] charArray1 = splits[1].toCharArray(); // what comes after E
            rString += charArray1[0] == '-'
                    ? "0." + "0".repeat(Character.digit(charArray1[1] - 1, 10)) + charArray0[0]
                    : charArray0[0] + "0".repeat(Character.digit(charArray1[1] - 1, 10));
            return Double.parseDouble(rString);
        }
        for (char i : charArray) {
            if (Character.isDigit(i) && i != '0') {
                return Double.parseDouble(nString.split(String.valueOf(i), 2)[0] + i);
            }
        }
        return 0;
    }

    /*
     * Counts the nuber of digits in a double n
     * Ignores '-' and '.'
     * e.g counDigits(-10.542) = 5, countDigits(76254) = 5
     */
    private int countDigits(double n) {
        return String.valueOf(n).replace("-", "").length() - 1;
    }

    /*
     * rounds a double n to a specific decimal place. e.g. roundTo(10.567, 1) =
     * 10.6, roundTo(10.563, 2) = 10.56
     * Always rounds towards positive infinity with the decimal 1 order of
     * magnitude/1 decimal place lower than specified for argument decimalPlaces
     */
    private double roundTo(double n, int decimalPlaces) {
        double pow = Math.pow(10, decimalPlaces);
        return Math.floor((n * pow) + 0.5) / pow;
    }

    // #endregion

    private GraphTypes graphType;
    private GraphData graphData;
    private int graphHeight;
    private int graphWidth;
    private int plotPoints;

    public JPanelGraphs(GraphTypes GRAPHTYPE, GraphData GRAPHDATA, int GRAPHWIDTH, int GRAPHHEIGHT) {

        graphType = GRAPHTYPE;
        graphData = GRAPHDATA;
        graphWidth = GRAPHWIDTH;
        graphHeight = GRAPHHEIGHT;
        plotPoints = graphData.length;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension currentSize = getSize();
                graphWidth = currentSize.width;
                graphHeight = currentSize.height;
                repaint();
            }

        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (graphType) {
            case GraphTypes.BAR:
                createBarGraph(g);
                break;
            case GraphTypes.LINE:
                createLineGraph(g);
                break;
            default:
                System.err.println("Graph Type needs to be of enum GraphTypes!");
        }
    }

    // #region lineGraph
    private void createLineGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        boolean drawPoint = true;
        Color crossAxisColor = Color.BLACK;
        Color mainAxisColor = Color.BLACK;
        Color gridLineColor = Color.GRAY;
        Color pointColor;
        Color lineColor;
        Color textColor = Color.GRAY;

        String mainAxisTitle = "Template Text";
        String crossAxisTitle = "Template Text";

        double mainAxisStep = graphData.xAxis[1] - graphData.xAxis[0];
        double crossAxisStep = graphData.yAxis[0][0];

        for (double[] yAxis : graphData.yAxis) {
            for (int i = 0; i < yAxis.length; i++) {
                if (yAxis[i] > crossAxisStep) {
                    crossAxisStep = yAxis[i];
                }
            }
        }
        crossAxisStep /= plotPoints;
        int digitCount = countDigits(crossAxisStep) - countDigits(Math.floor(crossAxisStep));
        crossAxisStep = (digitCount > 1 ? roundTo(crossAxisStep, 1) : crossAxisStep);

        double crossAxisLineWidth = 2;
        double mainAxisLineWidth = 2;

        double padding = 0;

        double markerWidth = 1;
        double markerHeight = 5;

        double lineWidth = 1;
        double pointDiameter = 3;

        if (mainAxisTitle != "" || crossAxisTitle != "") {
            padding += getTextHeight(g2d) * 1.15;
        }
        padding += markerHeight;
        padding += getTextWidth(g2d, String.valueOf(crossAxisStep));

        double xAxisWidth = graphWidth - padding * 2;
        double yAxisHeight = graphHeight - padding * 2;// + mainAxisLineWidth/2;

        double distanceBetweenXMarkers = (xAxisWidth - mainAxisLineWidth) / plotPoints;
        double distanceBetweenYMarkers = (yAxisHeight - crossAxisLineWidth) / plotPoints;

        // g.fillRect();
        // g.fillRect();
        g2d.setColor(crossAxisColor);
        Rectangle2D crossAxisRectangle = new Rectangle.Double(padding, padding, crossAxisLineWidth, yAxisHeight);
        // g2d.draw(crossAxisRectangle);
        g2d.fill(crossAxisRectangle);

        g2d.setColor(mainAxisColor);
        Rectangle2D mainAxisRectangle = new Rectangle.Double(padding, graphHeight - padding,
                xAxisWidth + markerWidth - distanceBetweenXMarkers, mainAxisLineWidth);
        // g2d.draw(mainAxisRectangle);
        g2d.fill(mainAxisRectangle);

        double xOrigin = padding + mainAxisLineWidth;
        double yOrigin = graphHeight - padding;

        double xx = xOrigin;// + distanceBetweenXMarkers/2;
        double xy = yOrigin + crossAxisLineWidth;
        double xWidth = markerWidth;
        double xHeight = markerHeight;

        double yx = xOrigin - markerHeight - crossAxisLineWidth;
        double yy = yOrigin;// + distanceBetweenYMarkers/2;
        double yWidth = markerHeight;
        double yHeight = markerWidth;

        // #region old stuff. keeping cause it took so long to make and might need it
        // later :C
        /*
         * if(drawToOrigin && graphData.length > 0) {
         * double slope = 0;
         * double intercept = graphData.yAxis[0];
         * if(graphData.length > 1) {
         * slope = (graphData.yAxis[1] - graphData.yAxis[0]) / (graphData.xAxis[1] -
         * graphData.xAxis[0]); // slope formula m = (y2-y1)/(x2-x1)
         * intercept = graphData.yAxis[0] - slope * graphData.xAxis[0]; //y intercept
         * formula b = y - mx
         * }
         * 
         * double lineX1 = xOrigin;// + (0/mainAxisStep) * distanceBetweenXMarkers;
         * double lineX2 = xOrigin + (graphData.xAxis[0]/mainAxisStep) *
         * distanceBetweenXMarkers;
         * 
         * double lineY1 = yOrigin - (intercept/crossAxisStep) *
         * distanceBetweenYMarkers;
         * double lineY2 = yOrigin - (graphData.yAxis[0]/crossAxisStep) *
         * distanceBetweenYMarkers;
         * 
         * double rad = Math.atan2(lineY2 - lineY1, lineX2-lineX1);
         * double xRot = ( ( lineWidth/2 ) * Math.sin(rad) ); //use sin for x rotation
         * for natural 90 degree rotation
         * double yRot = ( ( lineWidth/2 ) * Math.cos(rad) ); //same with cos. use cos
         * for y for natural 90 degree rotation
         * double x1 = lineX1;// + xRot; //ignore rotation here so the polygon leftmost
         * x doesnt extend past the axis line
         * double x4 = lineX2 + xRot;
         * double y1 = lineY1 - yRot;
         * double y4 = lineY2 - yRot;
         * 
         * xRot = ( ( lineWidth/2 ) * Math.sin(rad+Math.toRadians(180)) );
         * yRot = ( ( lineWidth/2 ) * Math.cos(rad+Math.toRadians(180)) );
         * 
         * double x2 = lineX1;//+xRot;
         * double x3 = lineX2+xRot;
         * 
         * double y2 = lineY1-yRot;
         * double y3 = lineY2-yRot;
         * //#region debugging stuff
         * double[] xPnts = {
         * x1, x2, x3, x4
         * };
         * double[] yPnts = {
         * y1, y2, y3, y4
         * };
         * for(int test = 0; test < 4; test++) {
         * Ellipse2D circ = new Ellipse2D.Double(xPnts[test] - pointDiameter/2,
         * yPnts[test] - pointDiameter/2, pointDiameter, pointDiameter);
         * g2d.draw(circ);
         * g2d.fill(circ);
         * //g.fillOval(xPnts[test], yPnts[test], pointDiameter, pointDiameter);
         * }
         * 
         * Ellipse2D circ = new Ellipse2D.Double(lineX1 - pointDiameter/2, lineY1 -
         * pointDiameter/2, pointDiameter, pointDiameter);
         * g2d.draw(circ);
         * g2d.fill(circ);
         * //#endregion
         * g2d.setColor(lineColor);
         * Path2D.Double poly = new Path2D.Double();
         * poly.moveTo(x1, y1);
         * poly.lineTo(x2, y2);
         * poly.lineTo(x3, y3);
         * poly.lineTo(x4, y4);
         * poly.closePath();
         * g2d.draw(poly); g2d.fill(poly);
         * //g.fillPolygon(xPnts, yPnts, 4);
         * }
         */// #endregion

        double textWidth = 0;
        for (int i = 0; i < plotPoints; i++) {
            // g.fillRect(yx, yy, yWidth, yHeight);
            // g.fillRect(xx, xy, xWidth, xHeight);
            g2d.setColor(gridLineColor);
            Rectangle2D crossAxisMarker = new Rectangle.Double(yx, yy, yWidth, yHeight);
            // g2d.draw(crossAxisMarker);
            g2d.fill(crossAxisMarker);

            Rectangle2D mainAxisMarker = new Rectangle.Double(xx, xy, xWidth, xHeight);
            // g2d.draw(mainAxisMarker);
            g2d.fill(mainAxisMarker);

            g2d.setColor(textColor);
            // main axis (x axis)
            drawAlignedString(g2d, String.valueOf(graphData.xAxis[i]), xx, xy + xHeight, CrossStringAlignments.TOP,
                    MainStringAlignments.CENTER);

            // cross axis (y axis)
            double textWidthTest = drawAlignedString(g2d, String.valueOf(roundTo(i * crossAxisStep, 1)), yx - yWidth,
                    yy, CrossStringAlignments.CENTER, MainStringAlignments.RIGHT);
            if (textWidthTest > textWidth) {
                textWidth = textWidthTest;
            }

            double lineX2 = xOrigin + ((graphData.xAxis[i] - 1) / mainAxisStep) * distanceBetweenXMarkers;
            double lineX1 = i > 0
                    ? xOrigin + ((graphData.xAxis[i - 1] - 1) / mainAxisStep) * distanceBetweenXMarkers
                    : lineX2;
            for (int yAxisInd = 0; yAxisInd < graphData.yAxis.length; yAxisInd++) {
                double[] yAxis = graphData.yAxis[yAxisInd];
                lineColor = graphData.colors[yAxisInd % graphData.colors.length];
                pointColor = graphData.colors[yAxisInd % graphData.colors.length];
                if (i < yAxis.length) {
                    double lineY2 = yOrigin - (yAxis[i] / crossAxisStep) * distanceBetweenYMarkers;
                    if (i > 0) {
                        double lineY1 = yOrigin - (yAxis[i - 1] / crossAxisStep) * distanceBetweenYMarkers;
                        double rad = Math.atan2(lineY2 - lineY1, lineX2 - lineX1);
                        double xRot = ((lineWidth / 2) * Math.sin(rad)); // use sin for x rotation for natural 90 degree
                                                                         // rotation
                        double yRot = ((lineWidth / 2) * Math.cos(rad)); // same with cos. use cos for y for natural 90
                                                                         // degree rotation
                        double x1 = lineX1 + xRot; // ignore rotation here so the polygon leftmost x doesnt extend past
                                                   // the axis line
                        double x4 = lineX2 + xRot;
                        double y1 = lineY1 - yRot;
                        double y4 = lineY2 - yRot;
                        xRot = ((lineWidth / 2) * Math.sin(rad + Math.toRadians(180))); // flipping 180 deg
                        yRot = ((lineWidth / 2) * Math.cos(rad + Math.toRadians(180)));
                        double x2 = lineX1 + xRot;
                        double x3 = lineX2 + xRot;
                        double y2 = lineY1 - yRot;
                        double y3 = lineY2 - yRot;
                        g2d.setColor(lineColor);
                        Path2D.Double poly = new Path2D.Double();
                        poly.moveTo(x1, y1);
                        poly.lineTo(x2, y2);
                        poly.lineTo(x3, y3);
                        poly.lineTo(x4, y4);
                        poly.closePath();
                        g2d.draw(poly);
                        g2d.fill(poly);
                    }
                    if (drawPoint) {
                        g2d.setColor(pointColor);
                        Ellipse2D circ = new Ellipse2D.Double(lineX2 - pointDiameter / 2, lineY2 - pointDiameter / 2,
                                pointDiameter, pointDiameter);
                        g2d.draw(circ);
                        g2d.fill(circ);
                    }

                }
            }

            xx += distanceBetweenXMarkers;
            yy -= distanceBetweenYMarkers;

        }
        g2d.setColor(gridLineColor);
        Rectangle2D crossAxisMarker = new Rectangle.Double(yx, yy, yWidth, yHeight);
        // g2d.draw(crossAxisMarker);
        g2d.fill(crossAxisMarker);

        g2d.setColor(textColor);
        double textWidthTest = drawAlignedString(g2d, String.valueOf(roundTo(plotPoints * crossAxisStep, 1)),
                yx - yWidth, yy, CrossStringAlignments.CENTER, MainStringAlignments.RIGHT);
        if (textWidthTest > textWidth) {
            textWidth = textWidthTest;
        }

        double titleTextHeight = getTextHeight(g2d);
        // draw graph titles
        drawRotatedAlignedString(g2d, crossAxisTitle, padding - (textWidth + markerHeight + 5), graphHeight / 2, 90,
                CrossStringAlignments.TOP, MainStringAlignments.CENTER);
        drawRotatedAlignedString(g2d, mainAxisTitle, graphWidth / 2,
                (padding + yAxisHeight + titleTextHeight + markerHeight), 0, CrossStringAlignments.TOP,
                MainStringAlignments.CENTER);
    }

    // #region barGraph
    private void createBarGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        @SuppressWarnings("unused")
        boolean drawPoint = true;
        @SuppressWarnings("unused")
        Color crossAxisColor = Color.BLACK;
        Color mainAxisColor = Color.BLACK;
        Color gridLineColor = Color.GRAY;
        @SuppressWarnings("unused")
        Color pointColor = Color.BLUE;
        @SuppressWarnings("unused")
        Color lineColor = Color.BLUE;
        Color textColor = Color.GRAY;

        String mainAxisTitle = "Template Text";
        String crossAxisTitle = "Template Text";

        double mainAxisStep = graphData.xAxis[1] - graphData.xAxis[0];
        double crossAxisStep = graphData.yAxis[0][0];
        for (double[] yAxis : graphData.yAxis) {
            for (int i = 0; i < yAxis.length; i++) {
                if (yAxis[i] > crossAxisStep) {
                    crossAxisStep = yAxis[i];
                }
            }
        }
        crossAxisStep /= plotPoints;
        int digitCount = countDigits(crossAxisStep) - countDigits(Math.floor(crossAxisStep));
        crossAxisStep = (digitCount > 1 ? roundTo(crossAxisStep, 1) : crossAxisStep);

        double crossAxisLineWidth = 2;
        double mainAxisLineWidth = 2;

        double padding = 5;

        double markerWidth = 1;
        double markerHeight = 5;

        if (mainAxisTitle != "" || crossAxisTitle != "") {
            padding += getTextHeight(g2d) * 1.15;
        }
        padding += markerHeight;
        padding += getTextWidth(g2d, String.valueOf(crossAxisStep));

        double xAxisWidth = graphWidth - padding * 2;
        double yAxisHeight = graphHeight - padding * 2;// + mainAxisLineWidth/2;

        double distanceBetweenXMarkers = (xAxisWidth - mainAxisLineWidth) / plotPoints;
        double distanceBetweenYMarkers = (yAxisHeight - crossAxisLineWidth) / plotPoints;

        g2d.setColor(mainAxisColor);
        Rectangle2D mainAxisRectangle = new Rectangle.Double(padding, graphHeight - padding,
                xAxisWidth + markerWidth + distanceBetweenXMarkers / 2, mainAxisLineWidth);
        // g2d.draw(mainAxisRectangle);
        g2d.fill(mainAxisRectangle);

        double xOrigin = padding + mainAxisLineWidth;
        double yOrigin = graphHeight - padding;

        double xx = xOrigin + distanceBetweenXMarkers * 0.75;// + distanceBetweenXMarkers/2;
        double xy = yOrigin;
        double xWidth = (distanceBetweenXMarkers * 0.75);

        double yx = xOrigin - markerHeight - crossAxisLineWidth;
        double yy = yOrigin;// + distanceBetweenYMarkers/2;
        double yWidth = (markerHeight + mainAxisLineWidth);
        double yHeight = markerWidth;

        double textWidth = 0;
        for (int i = 0; i < plotPoints; i++) {
            // g.fillRect(yx, yy, yWidth, yHeight);
            // g.fillRect(xx, xy, xWidth, xHeight);
            g2d.setColor(gridLineColor);
            Rectangle2D crossAxisMarker = new Rectangle.Double(yx, yy, yWidth, yHeight);
            // g2d.draw(crossAxisMarker);
            g2d.fill(crossAxisMarker);

            for (int yAxisInd = 0; yAxisInd < graphData.yAxis.length; yAxisInd++) {
                double[] yAxis = graphData.yAxis[yAxisInd];
                g2d.setColor(graphData.colors[yAxisInd % graphData.colors.length]);
                if (i < yAxis.length) {
                    double xHeight = (yAxis[i] / crossAxisStep) * distanceBetweenYMarkers;
                    double barXWidth = xWidth / graphData.yAxis.length;
                    Rectangle2D mainAxisMarker = new Rectangle.Double(xx - xWidth / 2 + yAxisInd * barXWidth,
                            xy - xHeight, barXWidth, xHeight);
                    // g2d.draw(mainAxisMarker);
                    g2d.fill(mainAxisMarker);
                }

            }

            g2d.setColor(textColor);
            // main axis (x axis)
            // drawAlignedString(g2d, String.valueOf(graphData.xAxis[i]), xx, xy,
            // CrossStringAlignments.TOP, MainStringAlignments.CENTER);
            // cross axis (y axis)
            double textWidthTest = drawAlignedString(g2d, String.valueOf(roundTo(i * crossAxisStep, 1)), yx - yWidth,
                    yy, CrossStringAlignments.CENTER, MainStringAlignments.RIGHT);
            if (textWidthTest > textWidth) {
                textWidth = textWidthTest;
            }

            xx += distanceBetweenXMarkers;
            yy -= distanceBetweenYMarkers;

        }
        g2d.setColor(gridLineColor);
        Rectangle2D crossAxisMarker = new Rectangle.Double(yx, yy, yWidth, yHeight);
        // g2d.draw(crossAxisMarker);
        g2d.fill(crossAxisMarker);

        g2d.setColor(textColor);
        double textWidthTest = drawAlignedString(g2d, String.valueOf(roundTo(plotPoints * crossAxisStep, 1)),
                yx - yWidth, yy, CrossStringAlignments.CENTER, MainStringAlignments.RIGHT);
        if (textWidthTest > textWidth) {
            textWidth = textWidthTest;
        }

        double titleTextHeight = getTextHeight(g2d);
        // draw graph titles
        drawRotatedAlignedString(g2d, crossAxisTitle, padding - (textWidth + markerHeight + 5), graphHeight / 2, 90,
                CrossStringAlignments.TOP, MainStringAlignments.CENTER);
        drawRotatedAlignedString(g2d, mainAxisTitle, graphWidth / 2,
                (padding + yAxisHeight + titleTextHeight + markerHeight), 0, CrossStringAlignments.TOP,
                MainStringAlignments.CENTER);
    }
}
