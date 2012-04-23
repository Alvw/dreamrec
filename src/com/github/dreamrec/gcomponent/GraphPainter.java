package com.github.dreamrec.gcomponent;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 */
public class GraphPainter implements IPainter {

    private IGraphPainterModel graphPainterModel;

    public GraphPainter(IGraphPainterModel graphPainterModel) {
        this.graphPainterModel = graphPainterModel;
    }

    public void paint(Graphics2D g) {
        AffineTransform previousTransform = g.getTransform();
        g.setTransform(AffineTransform.getScaleInstance(1.0, -1.0)); // y flip transformation  todo move to GComponentPainter
        //move the origin of coordinates to (xAxisPosition, yAxisPosition)
//        g.setTransform(AffineTransform.getTranslateInstance(graphPainterModel.getXAxisPosition(),graphPainterModel.getYAxisPosition()));
        int numberOfPoints = Math.min(graphPainterModel.getDataView().size() - graphPainterModel.getStartIndex(),graphPainterModel.getXSize());
        for (int i = 0; i < numberOfPoints; i++) {
            int value = (int)graphPainterModel.getYZoom()*graphPainterModel.getDataView().get(i+graphPainterModel.getStartIndex());
            g.drawLine(i,value,i,value);
        }
        g.setTransform(previousTransform);
    }
}