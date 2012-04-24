package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class GraphPainter implements IPainter<IGraphPainterModel> {

     public void paint(Graphics2D g, IGraphPainterModel paintModel) {
        int numberOfPoints = Math.min(paintModel.getDataView().size() - paintModel.getStartIndex(),paintModel.getXSize());
        for (int i = 0; i < numberOfPoints; i++) {
            int value = (int)paintModel.getYZoom()*paintModel.getDataView().get(i+paintModel.getStartIndex());
            g.drawLine(i,value,i,value);
        }
    }
}