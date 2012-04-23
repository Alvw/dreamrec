package com.github.dreamrec.gcomponent;

import com.github.dreamrec.gcomponent.GraphPainter;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 */
public class GComponentPainter implements IPainter{


    GComponentModel gModel;
    IPainter timePainter;
    IPainter yAxisPainter;
    IPainter graphPainter;

    public GComponentPainter(GComponentModel gModel) {
        this.gModel = gModel;
        yAxisPainter = new YAxisPainter(gModel);
        graphPainter = new GraphPainter(gModel);
    }

    public void paint(Graphics2D g) {
         AffineTransform previousTransform = g.getTransform();
        //move the origin of coordinates to (xAxisPosition, yAxisPosition)
        g.setTransform(AffineTransform.getTranslateInstance(gModel.getXAxisPosition(),gModel.getYAxisPosition()));
        timePainter.paint(g);
        yAxisPainter.paint(g);
        g.setTransform(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation
        graphPainter.paint(g);
        g.setTransform(previousTransform);
     }
}
