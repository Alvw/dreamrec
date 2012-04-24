package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class SlowTimePainter implements IPainter<ITimePainterModel> {

    private final int minute = 60000;//milliseconds
    private final int twoMinute = 2 * minute;//milliseconds
    private final int tenMinute = 10 * minute;//milliseconds
    private final int thirtyMinute = 30 * minute;//milliseconds
    private IPainter<PositionedLabel> pointPainter = new PointPainter();
    private IPainter<PositionedLabel> tPainter = new TPainter();
    private IPainter<PositionedLabel> trianglePainter = new TrianglePainter();
    private IPainter<PositionedLabel> timeStampPainter = new TimeStampPainter();

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");


    public void paint(Graphics2D g, ITimePainterModel timePainterModel) {
        if (timePainterModel.getStartGraphTime() == 0) return;
        drawMarks(g, minute, pointPainter, timePainterModel);
        drawMarks(g, twoMinute, tPainter, timePainterModel);
        drawMarks(g, tenMinute, trianglePainter, timePainterModel);
        drawMarks(g, thirtyMinute, timeStampPainter, timePainterModel);
    }


    private void drawMarks(Graphics2D g, int step, IPainter<PositionedLabel> markPainter, ITimePainterModel timePainterModel) {
        long markTime = (timePainterModel.getStartGraphTime() / step + 1) * step;
        while (markPosition(markTime,timePainterModel) < timePainterModel.getXSize()) {
            int position =  markPosition(markTime, timePainterModel);
            String label = dateFormat.format(new Date(markTime)) ;
            markPainter.paint(g, new PositionedLabel(position,label));
            markTime += step;
        }
    }

    private int markPosition(long markTime, ITimePainterModel timePainterModel) {
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

class PositionedLabel {
    private final int position;
    private final String label;

    PositionedLabel(int position, String label) {
        this.position = position;
        this.label = label;
    }

    public int getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }
}