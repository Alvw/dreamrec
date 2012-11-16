package com.github.dreamrec.gcomponent;

import com.github.dreamrec.Filter;

import java.awt.*;

/**
 *
 */
public class GraphPainter implements IPainter<IGraphPainterModel> {

    private Color[] graphColors = {Color.YELLOW, Color.RED, Color.BLUE};

    public void paint(Graphics2D g, IGraphPainterModel paintModel) {
        Filter<Short>[] dataViewArray = paintModel.getDataView();
        for (int i = 0; i < dataViewArray.length; i++) {
            g.setColor(graphColors[i]);
            paintSingleGraph(g, paintModel, dataViewArray[i]);
        }
    }

    private void paintSingleGraph(Graphics2D g, IGraphPainterModel paintModel, Filter<Short> points) {
        VerticalLine vLine = new VerticalLine(-1, 0);
        int size = points.size();
        int pointsPerScreen = paintModel.getXSize();
        int startIndex = paintModel.getStartIndex();
        int stopIndex = (startIndex + pointsPerScreen > size) ? size : startIndex + pointsPerScreen;
        for (int i = startIndex; i < stopIndex; i++) {
            drawVerticalLine(g, i - startIndex, valueToPixel(points.get(i), paintModel), vLine);
        }
    }

    private int valueToPixel(short value, IGraphPainterModel paintModel) {
        return (int) Math.round(value * paintModel.getYZoom());
    }

    private void drawVerticalLine(Graphics2D g, int x, int y, VerticalLine vLine) {
        vLine.setNewBounds(y);
        g.drawLine(x, vLine.min, x, vLine.max);
    }
}

class VerticalLine {
    int max;
    int min;

    VerticalLine(int min, int max) {
        this.max = max;
        this.min = min;
    }

    void setNewBounds(int y) {
        if (y >= min && y <= max) {
            min = max = y;
        } else if (y > max) {
            min = max + 1;
            max = y;
        } else if (y < min) {
            max = min - 1;
            min = y;
        }
    }
}