package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class GraphPainter implements Painter {

    private GraphPainterModel graphPainterModel;

    public GraphPainter(GraphPainterModel graphPainterModel) {
        this.graphPainterModel = graphPainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
