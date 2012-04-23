package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class SlowTimePainter implements IPainter {

    private final int minute = 60000;//milliseconds
    private final int twoMinute = 2 * minute;//millisexonds
    private final int tenMinute = 10 * minute;//millisexonds
    private final int thirtyMinute = 30 * minute;//millisexonds

    private ITimePainterModel timePainterModel;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public SlowTimePainter(ITimePainterModel timePainterModel) {
        this.timePainterModel = timePainterModel;
    }

    public void paint(Graphics2D g) {
        if (timePainterModel.getStartGraphTime() == 0) return;
        drawMark(g, minute, new PointPainter());
        drawMark(g, twoMinute, new TPainter());
        drawMark(g, tenMinute, new TrianglePainter());
        drawMark(g, thirtyMinute, new TimeStampPainter());
    }


    private void drawMark(Graphics2D g, int step, PeriodicPainter markPainter) {
        long markTime = (timePainterModel.getStartGraphTime() / step + 1) * step;
        int markPosition = (int)((markTime - timePainterModel.getStartGraphTime())*timePainterModel.getFrequency()/1000);

        while (markTime < timePainterModel.getXSize()) {
            markPainter.paint(g, markPosition);
            markTime += step;
        }
    }


    interface PeriodicPainter {
        public void paint(Graphics2D g, int x);
    }

    class PointPainter implements PeriodicPainter {
        public void paint(Graphics2D g, int x) {
            g.drawLine(x, 0, x, 0);
        }
    }

    class TPainter implements PeriodicPainter {
        public void paint(Graphics2D g, int x) {
            g.drawLine(x - 1, 0, x + 1, 0);
            g.drawLine(x, 0, x, 5);
        }
    }

    class TrianglePainter implements PeriodicPainter {
        public void paint(Graphics2D g, int x) {
            GeneralPath triangel = new GeneralPath();
            triangel.moveTo(x - 3, 0);
            triangel.lineTo(x + 3, 0);
            triangel.lineTo(x, 6);
            triangel.lineTo(x - 3, 0);
            g.fill(triangel);
        }
    }

    class TimeStampPainter implements PeriodicPainter {
        public void paint(Graphics2D g, int x) {
            long time = timePainterModel.getStartGraphTime() + (long)(x*1000/timePainterModel.getFrequency());
            String timeStamp = dateFormat.format(new Date(time));
            g.drawString(timeStamp, x - 15, +18);
        }
    }
}