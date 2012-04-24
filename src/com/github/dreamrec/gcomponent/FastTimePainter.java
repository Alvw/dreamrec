package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 */
public class FastTimePainter extends TimePainter implements IPainter<ITimePainterModel>{

 
    private IPainter<PositionedLabel> pointPainter = new PointPainter();
    private IPainter<PositionedLabel> strokePainter = new StrokePainter();
    private IPainter<PositionedLabel>  rectanglePainter= new RectanglePainter();
    private IPainter<PositionedLabel> timeStampPainter = new TimeStampPainter();

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");


    public void paint(Graphics2D g, ITimePainterModel timePainterModel) {
        if (timePainterModel.getStartGraphTime() == 0) return;
        drawMarks(g, HALF_SECOND, pointPainter, timePainterModel);
        drawMarks(g, SECOND, strokePainter, timePainterModel);
        drawMarks(g, TEN_SECONDS, rectanglePainter, timePainterModel);
        drawMarks(g, MINUTE, timeStampPainter, timePainterModel);
    }
}


