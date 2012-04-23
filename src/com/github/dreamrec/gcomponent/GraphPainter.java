package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class GraphPainter implements IPainter {

    private IGraphPainterModel graphPainterModel;

    public GraphPainter(IGraphPainterModel graphPainterModel) {
        this.graphPainterModel = graphPainterModel;
    }

    public void paint(Graphics2D g) {
        int numberOfPoints = Math.min(graphPainterModel.getDataView().size() - graphPainterModel.getStartIndex(),graphPainterModel.getXSize());
        for (int i = 0; i < numberOfPoints; i++) {
            int value = (int)graphPainterModel.getYZoom()*graphPainterModel.getDataView().get(i+graphPainterModel.getStartIndex());
            g.drawLine(i,value,i,value);
        }
    }
}