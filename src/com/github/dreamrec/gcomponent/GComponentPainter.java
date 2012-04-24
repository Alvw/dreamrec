package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 */
public abstract class GComponentPainter implements IPainter<GComponentModel> {


    protected IPainter timePainter;
    protected IPainter yAxisPainter;
    protected IPainter graphPainter;
    protected IPainter cursorPainter;

    public void GComponentPainter() {
        yAxisPainter = new YAxisPainter();
        graphPainter = new GraphPainter();
    }

    public void paint(Graphics2D g, GComponentModel gModel) {
        AffineTransform previousTransform = g.getTransform();
        //move the origin of coordinates to (xAxisPosition, yAxisPosition)
        g.setTransform(AffineTransform.getTranslateInstance(gModel.getXAxisPosition(), gModel.getYAxisPosition()));
        timePainter.paint(g, gModel);
        yAxisPainter.paint(g, gModel);
        g.setTransform(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation
        graphPainter.paint(g, gModel);
        cursorPainter.paint(g, gModel);
        g.setTransform(previousTransform);
    }


}
