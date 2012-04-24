package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class TimePainter {
    protected final static int HALF_SECOND = 500;//milliseconds
    protected final static int SECOND = 1000;//milliseconds
    protected final static int TEN_SECONDS = 10*SECOND;//milliseconds
    protected final static int MINUTE = 60*SECOND;//milliseconds
    protected final static int TWO_MINUTES = 2 * MINUTE;//milliseconds
    protected final static int TEN_MINUTES = 10 * MINUTE;//milliseconds
    protected final static int THIRTY_MINUTES = 30 * MINUTE;//milliseconds

    protected DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    protected void drawMarks(Graphics2D g, int step, IPainter<PositionedLabel> markPainter, ITimePainterModel timePainterModel) {
        long markTime = (timePainterModel.getStartGraphTime() / step + 1) * step;
        while (markPosition(markTime,timePainterModel) < timePainterModel.getXSize()) {
            int position =  markPosition(markTime, timePainterModel);
            String label = dateFormat.format(new Date(markTime)) ;
            markPainter.paint(g, new PositionedLabel(position,label));
            markTime += step;
        }
    }

    protected int markPosition(long markTime, ITimePainterModel timePainterModel) {
        return (int) ((markTime - timePainterModel.getStartGraphTime()) * timePainterModel.getFrequency() / 1000);
    }
}

class PointPainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel positionedLabel) {
        int x = positionedLabel.getPosition();
        g.drawLine(x, 0, x, 0);
    }
}

class TPainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel positionedLabel) {
        int x = positionedLabel.getPosition();
        g.drawLine(x - 1, 0, x + 1, 0);
        g.drawLine(x, 0, x, 5);
    }
}

class TrianglePainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel positionedLabel) {
        GeneralPath triangle = new GeneralPath();
        int x = positionedLabel.getPosition();
        triangle.moveTo(x - 3, 0);
        triangle.lineTo(x + 3, 0);
        triangle.lineTo(x, 6);
        triangle.lineTo(x - 3, 0);
        g.fill(triangle);
    }
}

class TimeStampPainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel paintModel) {
        g.drawString(paintModel.getLabel(), paintModel.getPosition() - 15, +18);
    }
}

class StrokePainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel positionedLabel) {
        int x = positionedLabel.getPosition();
        g.drawLine(x, -2, x, +2);
    }
}

class RectanglePainter implements IPainter<PositionedLabel> {
    public void paint(Graphics2D g, PositionedLabel positionedLabel) {
        int x = positionedLabel.getPosition();
        g.fillRect(x-1, -4, 3, 9);
    }
}

