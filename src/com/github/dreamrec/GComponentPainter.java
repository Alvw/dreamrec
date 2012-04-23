package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GraphPainter;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 */
public class GComponentPainter {

    protected int YAxisPosition = 20;
    protected int XAxisPosition = 20;
    private GraphPainter graphPainter;

    public GComponentPainter() {
    }

    public GComponentPainter(int YAxisPosition, int XAxisPosition) {
        this.YAxisPosition = YAxisPosition;
        this.XAxisPosition = XAxisPosition;
    }

     public void paint(Graphics2D g) {
         AffineTransform previousTransform = g.getTransform();
        g.setTransform(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation  todo move to GComponentPainter
        //move the origin of coordinates to (xAxisPosition, yAxisPosition)
//        g.setTransform(AffineTransform.getTranslateInstance(graphPainterModel.getXAxisPosition(),graphPainterModel.getYAxisPosition()));

     }

}
