package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 */
public class GComponentPainter implements IPainter {


    protected GComponentModel gModel;
    protected IPainter timePainter;
    protected IPainter yAxisPainter;
    protected IPainter graphPainter;
    protected IPainter cursorPainter;

    public GComponentPainter(GComponentModel gModel) {
        this.gModel = gModel;
        yAxisPainter = new YAxisPainter(gModel);
        graphPainter = new GraphPainter(gModel);
        if (gModel instanceof GComponentFastModel) {
            cursorPainter = new EmptyPainter();
            timePainter = new FastTimePainter(gModel);
        } else if (gModel instanceof GComponentSlowModel) {
            GComponentSlowModel gSlowModel = (GComponentSlowModel) gModel;
            cursorPainter = new CursorPainter(gSlowModel);
            timePainter = new SlowTimePainter(gModel);
        }
    }

    public void paint(Graphics2D g) {
        AffineTransform previousTransform = g.getTransform();
        //move the origin of coordinates to (xAxisPosition, yAxisPosition)
        g.setTransform(AffineTransform.getTranslateInstance(gModel.getXAxisPosition(), gModel.getYAxisPosition()));
        timePainter.paint(g);
        yAxisPainter.paint(g);
        g.setTransform(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation
        graphPainter.paint(g);
        cursorPainter.paint(g);
        g.setTransform(previousTransform);
    }
}
