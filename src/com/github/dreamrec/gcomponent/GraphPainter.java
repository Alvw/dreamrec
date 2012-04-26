package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;

import java.awt.*;

/**
 *
 */
public class GraphPainter implements IPainter<IGraphPainterModel> {

    public void paint(Graphics2D g, IGraphPainterModel paintModel) {
        IListView<Integer> points = paintModel.getDataView();
        int[] z = {-1, 0};
        int size = points.size();
        int pointsPerScreen = paintModel.getXSize();
        int startIndex = paintModel.getStartIndex();
        int stopIndex = (startIndex + pointsPerScreen > size) ? size : startIndex + pointsPerScreen;
        for (int i = startIndex; i < stopIndex; i++) {
            vertgraf(g, i - startIndex, valueToPixel(points.get(i), paintModel), z);
        }
    }

    private int valueToPixel(Integer value, IGraphPainterModel paintModel) {
        return (int) Math.round(value * paintModel.getYZoom());
    }

    private void vertgraf(Graphics2D g, int x, int y, int[] z) {
        if ((y == z[0] && y == z[1])) {
            point(g, x, y);
            return;
        }

        if (y >= z[0] && y <= z[1]) {
            z[0] = y;
            z[1] = y;
            point0(g, x - 1, y);
            point(g, x, y);
            return;
        }

        if (y > z[1]) {
            z[0] = z[1] + 1;
            z[1] = y;
            vertline(g, x, z[0], y);
            return;
        }

        if (y < z[0]) {
            z[1] = z[0] - 1;
            z[0] = y;
            vertline(g, x, y, z[1]);
            return;
        }
    }

    private void vertline(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
    }

    private void point(Graphics2D g, int x, int y) {
        g.drawLine(x, y, x, y);
    }

    private void point0(Graphics2D g, int x, int y) {
        Color savedColor = g.getColor();
        g.setColor(g.getBackground());
        g.drawLine(x, y, x, y);
        g.setColor(savedColor);
    }

}