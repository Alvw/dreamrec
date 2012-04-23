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
        long minValue = timePainterModel.getStartGraphTime();
        for (int i = 1; i < timePainterModel.getXSize() * 1440000 / step + 1; i++) {
            int value = (minValue / step) * step + i * step;
            int relativeIndex = (int) ((value - minValue) / 12000);


            paintPoint(g, 60000);
            drawMark(g, 60000, timePainterModel.getXSize(), oneMinuteMarkBuilder);
            drawMark(g, 120000, timePainterModel.getXSize(), twoMinutesMarkBuilder);
            drawMark(g, 600000, timePainterModel.getXSize(), tenMinutesMarkBuilder);
            drawMark(g, 1800000, timePainterModel.getXSize(), slowTimeStampsBuilder);
        }
    }

    private void drawMark1(Graphics2D g, int step, IPainter markPainter) {
        int markPosition = (startValue / step) * step;

        while (markPosition < timePainterModel.getXSize()) {
            markPainter.paint(g, markPosition);
            markPosition += step;
        }
    }

    protected void drawMark(Graphics2D g, int step, int xSize, MarkBuilder markBuilder) {
        long minValue = timePainterModel.getStartGraphTime();
        for (int i = 1; i < xSize * 1440000 / step + 1; i++) {
            long value = (minValue / step) * step + i * step;
            int relativeIndex = (int) ((value - minValue) / 12000);
            markBuilder.buildMark(g, relativeIndex, value);

            if (timePainterModel.getStartGraphTime() == 0) return;
            paintPoint(g, 60000);
            paintT(g, 120000, timePainterModel.getXSize());
            drawMark(g, 600000, timePainterModel.getXSize(), tenMinutesMarkBuilder);
            drawMark(g, 1800000, timePainterModel.getXSize(), slowTimeStampsBuilder);

        }
    }

    private void paintPoint(Graphics2D g, int x) {
        g.drawLine(x, 0, x, 0);
    }

    private void paintT(Graphics2D g, int x) {
        g.drawLine(x - 1, 0, x + 1, 0);
        g.drawLine(x, 0, x, 5);
    }

    private void paintTriangle(Graphics2D g, int x) {
        GeneralPath triangel = new GeneralPath();
        triangel.moveTo(x - 3, 0);
        triangel.lineTo(x + 3, 0);
        triangel.lineTo(x, 6);
        triangel.lineTo(x - 3, 0);
        g.fill(triangel);
    }

    private void paintTime(Graphics2D g, int x, long time) {
        String timeStamp = dateFormat.format(new Date(time));
        g.drawString(timeStamp, x - 15, +18);
    }
}