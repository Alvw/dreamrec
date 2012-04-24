package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 */
public class SlowTimePainter extends TimePainter implements IPainter<ITimePainterModel> {

    private IPainter<PositionedLabel> pointPainter = new PointPainter();
    private IPainter<PositionedLabel> tPainter = new TPainter();
    private IPainter<PositionedLabel> trianglePainter = new TrianglePainter();
    private IPainter<PositionedLabel> timeStampPainter = new TimeStampPainter();


    public void paint(Graphics2D g, ITimePainterModel timePainterModel) {
        if (timePainterModel.getStartGraphTime() == 0) return;
        drawMarks(g, MINUTE, pointPainter, timePainterModel);
        drawMarks(g, TWO_MINUTES, tPainter, timePainterModel);
        drawMarks(g, TEN_MINUTES, trianglePainter, timePainterModel);
        drawMarks(g, THIRTY_MINUTES, timeStampPainter, timePainterModel);
    }
}




