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
        throw new UnsupportedOperationException("todo");
    }
}
