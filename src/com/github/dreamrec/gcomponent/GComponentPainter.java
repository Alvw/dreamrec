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

    public GComponentPainter() {
        yAxisPainter = new YAxisPainter();
        graphPainter = new GraphPainter();
    }

    public void paint(Graphics2D g, GComponentModel gModel) {
        AffineTransform previousTransform = g.getTransform();
        //move the origin of coordinates
        AffineTransform transform = null;
        if(gModel.isXCentered){
            transform = AffineTransform.getTranslateInstance(gModel.getLeftIndent(), gModel.getYSize()/2 + gModel.getTopIndent());
        } else{
            transform = AffineTransform.getTranslateInstance(gModel.getLeftIndent(), gModel.getYSize() + gModel.getTopIndent());
        }
        g.setTransform(transform);
        timePainter.paint(g, gModel);
        yAxisPainter.paint(g, gModel);
        transform.concatenate(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation
        g.setTransform(transform);
        graphPainter.paint(g, gModel);
        cursorPainter.paint(g, gModel);
        g.setTransform(previousTransform);
    }


}
